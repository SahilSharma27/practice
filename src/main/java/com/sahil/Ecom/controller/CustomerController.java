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

@RestController
public class CustomerController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/users/address",params = "role=customer")
    public ResponseEntity<String> addAddress(@RequestBody AddressDTO addressDTO){
        if(userService.checkUserEmail(addressDTO.getUserEmail())){
            //add address to the table

            return new ResponseEntity<>("Address ADDED", HttpStatus.OK);
        }

        return new ResponseEntity<>("USER NOT FOUND", HttpStatus.NOT_FOUND);

    }

}
