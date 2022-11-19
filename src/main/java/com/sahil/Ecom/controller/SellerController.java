package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.service.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private SellerService sellerService;


    Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping(value = "/register",params = "role=seller")
    public ResponseEntity<String> registerSeller(@RequestBody SellerDTO sellerDTO){


        //unique email
        if(userService.checkUserEmail(sellerDTO.getEmail())){
            return new ResponseEntity<>("User email already registered", HttpStatus.BAD_REQUEST);
        }

        //unique company name
        if(sellerService.checkSellerCompanyName(sellerDTO.getCompanyName())){
            return new ResponseEntity<>("Company name already exists",HttpStatus.BAD_REQUEST);
        }

        //unique gst
        if(sellerService.checkSellerGst(sellerDTO.getGst())){
            return new ResponseEntity<>("Gst already exists",HttpStatus.BAD_REQUEST);
        }

//      check pass and cpass
        if(!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword()))
            return new ResponseEntity<>("Password and confirm password doesn't match",HttpStatus.BAD_REQUEST);

//        1)save user
        sellerService.register(sellerDTO);

        //send acknowledgment
       // userService.sendSellerAcknowledgement(sellerDTO.getEmail());

        return new ResponseEntity<>("User registered Successfully", HttpStatus.OK);

    }



}
