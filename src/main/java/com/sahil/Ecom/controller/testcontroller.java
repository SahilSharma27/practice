package com.sahil.Ecom.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {

    @GetMapping("/welcome")
    public String  welcome(){
        return "Lets go";
    }
}
