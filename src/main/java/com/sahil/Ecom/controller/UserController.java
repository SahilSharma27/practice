package com.sahil.Ecom.controller;

import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/users",params = "role=customer")
    public Iterable<Customer> getAllCustomers(){
        return userService.getAllCustomers();
    }

    @GetMapping(value = "/users",params = "role=seller")
    public Iterable<Seller> getAllSellers(){
        return userService.getAllSellers();
    }


    @PostMapping(value = "/register",params = "role=customer")
    public Customer registerCustomer(@RequestBody Customer customer){
        return userService.register(customer);
    }

    @PostMapping(value = "/register",params = "role=seller")
    public Seller registerSeller(@RequestBody Seller seller){
        return userService.register(seller);
    }

    @PutMapping(value = "users/activate/{id}")
    public User activate(@PathVariable Long id){
        return userService.activate(id);
    }
}
