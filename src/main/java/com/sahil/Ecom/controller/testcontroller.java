package com.sahil.Ecom.controller;


import com.sahil.Ecom.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {

    @Autowired
    EmailSenderService emailSenderService;

    @GetMapping("/welcome")
    public String  welcome(){
        emailSenderService.sendEmail("sharma.sahil1560@gmail.com","TEST","TEST");

        return "Lets go";
    }




}
