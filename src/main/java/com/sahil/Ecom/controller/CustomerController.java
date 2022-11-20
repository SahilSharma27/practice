package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.CustomerDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.exception.EmailAlreadyRegisteredException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.service.CustomerService;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;

@RestController
public class CustomerController {

    @Autowired
    UserService userService;

    @Autowired
    CustomerService customerService;

    @Autowired
    private MessageSource messageSource;


    @PostMapping(value = "/register",params = "role=customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws Exception {

        Locale locale = LocaleContextHolder.getLocale();

//      Check if email taken
        if(userService.checkUserEmail(customerDTO.getEmail())){
            throw new EmailAlreadyRegisteredException(messageSource.getMessage("email.already.registered", null, "message", locale));
        }

//      check pass and cpass
        if(!customerDTO.getPassword().equals(customerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException(messageSource.getMessage("password.confirmPassword", null, "message", locale));
            //return new ResponseEntity<>("Password and confirm password doesn't match",HttpStatus.BAD_REQUEST);

//        1)save user
        customerService.register(customerDTO);

//        2)send activation link
           userService.activationHelper(customerDTO.getEmail());

        ResponseDTO responseDTO =  new ResponseDTO();
        responseDTO.setTimestamp(new Date());

        responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));
//        responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));

        responseDTO.setResponseStatusCode(200);
        return ResponseEntity.ok(responseDTO);
    }




//    @PostMapping(value = "/users/address",params = "role=customer")
//    public ResponseEntity<String> addAddress(@RequestBody AddressDTO addressDTO){
//        if(userService.checkUserEmail(addressDTO.getUserEmail())){
//            //add address to the table
//
//            return new ResponseEntity<>("Address ADDED", HttpStatus.OK);
//        }
//
//        return new ResponseEntity<>("USER NOT FOUND", HttpStatus.NOT_FOUND);
//
//    }

}
