package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.LoginRequestDTO;
import com.sahil.Ecom.entity.LoginResponseDTO;
import com.sahil.Ecom.exception.EmailAlreadyRegisteredException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.CustomerService;
import com.sahil.Ecom.service.LoginService;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    LoginService loginService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    TokenGeneratorHelper tokenGeneratorHelper;

    Locale locale = LocaleContextHolder.getLocale();

    @PostMapping(value = "/register", params = "role=customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) {

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

            return ResponseEntity.ok(responseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @PostMapping(value = "/login", params = "role=customer")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {


//        loginService.removeAlreadyGeneratedTokens(loginRequestDTO);
//
//        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);
//
//        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());
        LoginResponseDTO loginResponseDTO = customerService.loginCustomer(loginRequestDTO);
        if(loginResponseDTO!=null){
                    return ResponseEntity.ok(loginResponseDTO);
        }

        return new ResponseEntity<>("PROBLEM",HttpStatus.BAD_REQUEST);
//        return ResponseEntity.ok(loginResponseDTO);
    }



    @PostMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> addAddress(@RequestBody AddressDTO addressDTO, HttpServletRequest request) {

        //get token
        //get email form token
        //get user from email
        //add new address to that user

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());


        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer".length());

            try {

                userEmail = this.jwtUtil.extractUsername(accessToken);

            } catch (Exception e) {

                e.printStackTrace();

            }

            if (customerService.addAddressToCustomer(userEmail, addressDTO)) {
                responseDTO.setMessage(messageSource.getMessage("customer.address.added", null, "message", locale));
                responseDTO.setResponseStatusCode(HttpStatus.OK);
                return ResponseEntity.ok(responseDTO);
            }

            responseDTO.setMessage(messageSource.getMessage("user.not.found", null, "message", locale));
            responseDTO.setResponseStatusCode(HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);

        }

        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);

    }


    @GetMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> getAddressList(HttpServletRequest request) {

        //get token
        //get email form token
        //get user from email
        //get address form user

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer".length());

            try {
                userEmail = this.jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Address> addresses = customerService.getAllCustomerAddresses(userEmail);

            return ResponseEntity.ok(addresses);

        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping(value = "/users/address/{addressId}", params = "role=customer")
    public ResponseEntity<?> deleteAddress(@PathVariable (name = "addressId") Long id) {

    //find address by id and delete
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());

        if(customerService.removeAddress(id)){

            responseDTO.setMessage(messageSource.getMessage("address.deleted",null,"message",locale));
            responseDTO.setResponseStatusCode(HttpStatus.OK);
            return ResponseEntity.ok(responseDTO);

        }


        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(responseDTO, HttpStatus.FORBIDDEN);
    }


    @GetMapping(value = "users/profile", params = "role=customer")
    public ResponseEntity<?> getCustomerProfile(HttpServletRequest request) {
        //get token
        //get username
        //get profile

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer".length());

            try {
                userEmail = this.jwtUtil.extractUsername(accessToken);
                FetchCustomerDTO fetchCustomerDTO =  customerService.fetchCustomerProfileDetails(userEmail);
                return ResponseEntity.ok(fetchCustomerDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("",HttpStatus.NOT_FOUND);

    }

    @PatchMapping(value = "/users/update/profile")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerProfileDTO customerProfileDTO,HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer".length());
            username = jwtUtil.extractUsername(accessToken);

            customerService.updateProfile(username,customerProfileDTO);

        }

        return new ResponseEntity<>("HELLO",HttpStatus.OK);
    }






}
