package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.dto.customer.CustomerDTO;
import com.sahil.Ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.Ecom.dto.LoginRequestDTO;
import com.sahil.Ecom.dto.LoginResponseDTO;
import com.sahil.Ecom.exception.EmailAlreadyRegisteredException;
import com.sahil.Ecom.exception.InvalidTokenException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;


@RestController
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;


    @Autowired
    private ProductService productService;

    Locale locale = LocaleContextHolder.getLocale();

    Logger logger  = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping(value = "/register", params = "role=customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) {

        logger.info("Registering customer.."+customerDTO.getEmail());

        //check pass and cpass
        if (!customerDTO.getPassword().equals(customerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException();


        //Check if email taken
        if (userService.checkUserEmail(customerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        //1)save user
        if (customerService.register(customerDTO)) {

            //2)send activation link
            userService.activationHelper(customerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));
            logger.info("Successfully registered.."+customerDTO.getEmail());
            return ResponseEntity.ok(responseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @PostMapping(value = "/login", params = "role=customer")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        logger.info("Logging In..."+loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = customerService.loginCustomer(loginRequestDTO);
        if (loginResponseDTO != null) {
            logger.info("Logged In..."+loginRequestDTO.getUsername());
            return ResponseEntity.ok(loginResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("login.failed", null, "message", locale));
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @PostMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressDTO addressDTO, HttpServletRequest request) {

        //get token
        //get email form token
        //get user from email
        //add new address to that user

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;


        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer ".length());

            try {

                userEmail = this.jwtUtil.extractUsername(accessToken);

            } catch (Exception e) {

                e.printStackTrace();
                throw new InvalidTokenException();

            }

        }


        customerService.addAddressToCustomer(userEmail, addressDTO);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setMessage(messageSource.getMessage("customer.address.added", null, "message", locale));
        responseDTO.setResponseStatusCode(HttpStatus.OK);

        return ResponseEntity.ok(responseDTO);


    }


    @GetMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> getAddressList(HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer".length());

            try {
                userEmail = this.jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }
        }
        return ResponseEntity.ok(customerService.getAllCustomerAddresses(userEmail));
    }

    @DeleteMapping(value = "/users/address/{addressId}", params = "role=customer")
    public ResponseEntity<?> deleteAddress(@PathVariable(name = "addressId") Long id) {

        //find address by id and delete

        customerService.removeAddress(id);

        String message = messageSource.getMessage("address.deleted", null, "message", locale);

        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));

    }


    @GetMapping(value = "users/profile", params = "role=customer")
    public ResponseEntity<?> getCustomerProfile(HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer ".length());

            try {
                userEmail = this.jwtUtil.extractUsername(accessToken);

            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }
        }
        return ResponseEntity.ok(customerService.fetchCustomerProfileDetails(userEmail));


    }

    @PatchMapping(value = "/users/update/profile", params = "role=customer")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerProfileUpdateDTO customerProfileUpdateDTO, HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }

        }

        customerService.updateProfile(username, customerProfileUpdateDTO);
        String message = messageSource.getMessage("user.profile.updated", null, "message", locale);
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));


    }


    @GetMapping(value = "/category", params = "role=customer")
    public ResponseEntity<?> fetchAllCategoriesForCustomer(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        return ResponseEntity.ok(customerService.getAllCategoriesForCustomer(categoryId));
    }


    @GetMapping(value = "/products/{product_id}",params = "role=customer")
    public ResponseEntity<?> getProductForCustomer(@PathVariable("product_id")Long productId) {
        return ResponseEntity.ok(
                productService
                        .getProductForCustomer(productId));

    }


    @GetMapping(value = "/products",params = "role=customer")
    public ResponseEntity<?> getAllProductsCustomer(
            @RequestParam(value= "category_id")String categoryId,
            @RequestParam(value = "page",defaultValue = "0") String page,
            @RequestParam(value = "size",defaultValue = "10")String size,
            @RequestParam(value = "sort",defaultValue = "id")String sort,
            @RequestParam(value = "order",defaultValue = "asc")String order) {


        return ResponseEntity.ok(
                productService
                        .getAllProductsForCustomer(
                                Long.parseLong(categoryId)
                                ,Integer.parseInt(page)
                                ,Integer.parseInt(size)
                                ,sort
                                ,order));

    }

}
