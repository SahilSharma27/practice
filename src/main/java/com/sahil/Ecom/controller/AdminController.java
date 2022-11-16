package com.sahil.Ecom.controller;

import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);



    @GetMapping(value = "/users",params = "role=admin")
    public Iterable<User> getAllUsers(){return userService.getAllUsers();}

    @GetMapping(value = "/users/customers")
    public Iterable<Customer> getAllCustomers(){return userService.getAllCustomers();
    }

    @GetMapping(value = "/users/sellers")
    public Iterable<Seller> getAllSellers(){
        return userService.getAllSellers();
    }





}
