package com.sahil.ecom.controller;


import com.sahil.ecom.dto.AddAddressDTO;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.ResponseDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.ecom.enums.EcomRoles;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.service.CustomerService;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.ProductService;
import com.sahil.ecom.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;


@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final UserService userService;
    private final CustomerService customerService;
    private final LoginService loginService;
    private final MessageSource messageSource;
    private final ProductService productService;

    @PostMapping(value = "/register", params = "role=customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody AddCustomerDTO addCustomerDTO) {
        //check pass and cpass
        if (!addCustomerDTO.getPassword().equals(addCustomerDTO.getConfirmPassword()))
            throw new GenericException("Password confirm password not matching");


        //Check if email taken
        if (userService.checkUserEmail(addCustomerDTO.getEmail())) {
            throw new GenericException("Email already registered");
        }

        //1)save user
        if (customerService.register(addCustomerDTO)) {

            //2)send activation link
            userService.activationHelper(addCustomerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", LocaleContextHolder.getLocale()));
            log.info("Successfully registered.." + addCustomerDTO.getEmail());
            return ResponseEntity.ok(responseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @PostMapping(value = "/login", params = "role=customer")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        log.info("Logging In..." + loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = customerService.loginCustomer(loginRequestDTO);
        if (loginResponseDTO != null) {
            log.info("Logged In..." + loginRequestDTO.getUsername());
            return ResponseEntity.ok(loginResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("login.failed", null, "message", LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @PostMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddAddressDTO addAddressDTO) {
        customerService.addAddressToCustomer(addAddressDTO);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setMessage(messageSource.getMessage("customer.address.added", null, "message", LocaleContextHolder.getLocale()));
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        return ResponseEntity.ok(responseDTO);
    }


    @GetMapping(value = "/users/address", params = "role=customer")
    public ResponseEntity<?> getAddressList() {
        return ResponseEntity.ok(customerService.getAllCustomerAddresses());
    }

    @DeleteMapping(value = "/users/address/{addressId}", params = "role=customer")
    public ResponseEntity<?> deleteAddress(@PathVariable(name = "addressId") Long id) {
        customerService.removeAddress(id);
        String message = messageSource.getMessage("address.deleted", null, "message", LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
    }


    @GetMapping(value = "users/profile", params = "role=customer")
    public ResponseEntity<?> getCustomerProfile() {
        return ResponseEntity.ok(customerService.fetchCustomerProfileDetails());
    }

    @PatchMapping(value = "/users/update/profile", params = "role=customer")
    public ResponseEntity<?> updateCustomerProfile(@Valid @RequestBody CustomerProfileUpdateDTO customerProfileUpdateDTO) {
        customerService.updateProfile(customerProfileUpdateDTO);
        String message = messageSource.getMessage("user.profile.updated", null, "message", LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));

    }


    @GetMapping(value = "/categories", params = "role=customer")
    public ResponseEntity<?> fetchAllCategoriesForCustomer(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        String role = userService.getRole();
        if (role.equals(EcomRoles.CUSTOMER.role)) {
            return ResponseEntity.ok(customerService.getAllCategoriesForCustomer(categoryId));
        } else
            throw new InvalidTokenException();

    }


    @GetMapping(value = "/products/{product_id}", params = "role=customer")
    public ResponseEntity<?> getProductForCustomer(@PathVariable("product_id") Long productId) {
        return ResponseEntity.ok(
                productService
                        .getProductForCustomer(productId));

    }


    @GetMapping(value = "/products", params = "role=customer")
    public ResponseEntity<?> getAllProductsCustomer(
            @RequestParam(value = "category_id") String categoryId,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {


        return ResponseEntity.ok(
                productService
                        .getAllProductsForCustomer(
                                Long.parseLong(categoryId)
                                , Integer.parseInt(page)
                                , Integer.parseInt(size)
                                , sort
                                , order));

    }

}
