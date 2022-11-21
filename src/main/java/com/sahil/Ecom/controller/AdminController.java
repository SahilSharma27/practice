package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ActivateSellerDTO;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.exception.TokenExpiredException;
import com.sahil.Ecom.helper.JwtUtil;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private EmailSenderService emailSenderService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request, HttpServletResponse response){

//        Iterable<User>
//        logger.info("------------GETTTING");
//        String tokenHeader = request.getHeader("Authorization");
//
//        String accessToken = null;
//
//        //check format
//        if (tokenHeader != null && tokenHeader.startsWith("Bearer"))
//            accessToken = tokenHeader.substring("Bearer".length());
//
//        if(jwtUtil.isTokenExpired(accessToken)){
//            throw new TokenExpiredException("TOKENNNN EX");
//        }

        return new ResponseEntity<Iterable<User>>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping(value = "/users/customers")
    public Iterable<Customer> getAllCustomers(){

        return userService.getAllCustomers();
    }

    @GetMapping(value = "/users/sellers")
    public Iterable<Seller> getAllSellers(){
        return userService.getAllSellers();
    }


    @PostMapping(value = "users/seller/activate")
    public ResponseEntity<String> activateSeller(@RequestBody ActivateSellerDTO activateSellerDTO){

        if(userService.checkUserEmail(activateSellerDTO.getUserEmail())){
            userService.activate(activateSellerDTO.getUserEmail());
            return new ResponseEntity<>("SELLER Account Activated", HttpStatus.OK);
        }
        return new ResponseEntity<>("SELLER Account NOT FOUND", HttpStatus.NOT_FOUND);

    }


}
