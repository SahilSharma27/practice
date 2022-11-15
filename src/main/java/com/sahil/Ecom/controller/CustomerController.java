package com.sahil.Ecom.controller;


import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    UserService userService;





}
