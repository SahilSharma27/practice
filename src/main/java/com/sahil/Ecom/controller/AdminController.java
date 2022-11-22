package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ActivateSellerDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;


@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private MessageSource messageSource;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    Locale locale = LocaleContextHolder.getLocale();



    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<Iterable<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/users/customers")
    public ResponseEntity<?> getAllCustomers() {
        return new ResponseEntity<>(userService.getAllCustomers(), HttpStatus.OK);

    }

    @GetMapping(value = "/users/sellers")
    public ResponseEntity<?> getAllSellers() {
        return new ResponseEntity<>(userService.getAllSellers(), HttpStatus.OK);
    }

    @PutMapping(value = "users/activate/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable(name = "id") Long sellerId) {

        String message;

        if (userService.activateAccount(sellerId)) {

            message = messageSource.getMessage("user.account.activated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(new Date(), message, 200));

        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(new Date(), message, 404), HttpStatus.NOT_FOUND);


    }


    @PutMapping(value = "users/deactivate/{id}")
    public ResponseEntity<?> deActivateSeller(@PathVariable(name = "id") Long sellerId) {

        String message;

        if (userService.deActivateAccount(sellerId)){

            message = messageSource.getMessage("user.account.deactivated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(new Date(), message, 200));

        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(new Date(), message, 404), HttpStatus.NOT_FOUND);

    }

}
