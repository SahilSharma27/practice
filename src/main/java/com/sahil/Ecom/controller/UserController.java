package com.sahil.Ecom.controller;

import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping(value = "/users",params = "role=customer")
    public Iterable<Customer> getAllCustomers(){return userService.getAllCustomers();
    }

    @GetMapping(value = "/users",params = "role=seller")
    public Iterable<Seller> getAllSellers(){
        return userService.getAllSellers();
    }

    @GetMapping(value = "/users",params = "role=admin")
    public Iterable<User> getAllUsers(){
        return userService.getAllUsers();
    }


    @PostMapping(value = "/register",params = "role=customer")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) throws Exception {

        if(userService.checkUserEmail(customer.getEmail())){
            return new ResponseEntity<>("User email already registered", HttpStatus.BAD_REQUEST);
        }

        userService.register(customer);

        return new ResponseEntity<>("User registered Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/register",params = "role=seller")
    public Seller registerSeller(@RequestBody Seller seller){
        return userService.register(seller);
    }



    @PostMapping(value = "/login",params = "role=admin")
    public ResponseEntity<?> loginAdmin(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        //send this token to email
        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/login",params = "role=customer")
    public ResponseEntity<?> loginCustomer(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        //send this token to email
        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/login",params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        //send this token to email
        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @PutMapping(value = "users/activate/{id}")
    public User activate(@PathVariable Long id){
        return userService.activate(id);
    }






}
