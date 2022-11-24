package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.exception.EmailAlreadyRegisteredException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.service.CustomerService;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    JwtUtil jwtUtil;

    Locale locale = LocaleContextHolder.getLocale();

    @PostMapping(value = "/register", params = "role=customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws Exception {

        Locale locale = LocaleContextHolder.getLocale();

//      Check if email taken
        if (userService.checkUserEmail(customerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException(messageSource.getMessage("email.already.registered", null, "message", locale));
        }

//      check pass and cpass
        if (!customerDTO.getPassword().equals(customerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException(messageSource.getMessage("password.confirmPassword", null, "message", locale));


//        1)save user
        customerService.register(customerDTO);

//        2)send activation link
        userService.activationHelper(customerDTO.getEmail());

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(new Date());

        responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));
        responseDTO.setResponseStatusCode(200);
        return ResponseEntity.ok(responseDTO);


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
        responseDTO.setTimestamp(new Date());


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
                responseDTO.setResponseStatusCode(200);
                return ResponseEntity.ok(responseDTO);
            }

            responseDTO.setMessage(messageSource.getMessage("user.not.found", null, "message", locale));
            responseDTO.setResponseStatusCode(404);

            return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);

        }

        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(400);

        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);

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
        responseDTO.setTimestamp(new Date());
        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(400);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/users/address/{addressId}", params = "role=customer")
    public ResponseEntity<?> deleteAddress(@PathVariable (name = "addressId") Long id) {

    //find address by id and delete
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(new Date());

        if(customerService.removeAddress(id)){

            responseDTO.setMessage(messageSource.getMessage("address.deleted",null,"message",locale));
            responseDTO.setResponseStatusCode(200);
            return ResponseEntity.ok(responseDTO);

        }


        responseDTO.setMessage("INVALID TOKEN");
        responseDTO.setResponseStatusCode(400);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
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
