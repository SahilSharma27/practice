package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CustomerController {

    @Autowired
    UserService userService;


    @PostMapping(value = "/register",params = "role=customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody Customer customer) throws Exception {

//      Check if email taken
        if(userService.checkUserEmail(customer.getEmail())){
            return new ResponseEntity<>("User email already registered", HttpStatus.BAD_REQUEST);
        }

//      check pass and cpass

        if(!customer.getPassword().equals(customer.getConfirmPassword()))
            return new ResponseEntity<>("Password and confirm password doesn't match",HttpStatus.BAD_REQUEST);

//        1)save user
        userService.register(customer);

//        2)send activation link
     //   userService.activationHelper(customer.getEmail());

        return new ResponseEntity<>("User registered Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/users/address",params = "role=customer")
    public ResponseEntity<String> addAddress(@RequestBody AddressDTO addressDTO){
        if(userService.checkUserEmail(addressDTO.getUserEmail())){
            //add address to the table

            return new ResponseEntity<>("Address ADDED", HttpStatus.OK);
        }

        return new ResponseEntity<>("USER NOT FOUND", HttpStatus.NOT_FOUND);

    }

}
