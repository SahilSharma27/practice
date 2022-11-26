package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.CustomerService;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private CustomerService customerService;

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


    @GetMapping(value = "/users/customersPaged")
    public ResponseEntity<?> getAllCustomersPaged(HttpServletRequest request) {

        String email = request.getParameter("email");

        if(email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new ResponseEntity<>(userService.getAllCustomersPaged(page, size, sort), HttpStatus.OK);

        }else{

            return ResponseEntity.ok(customerService.fetchCustomerProfileDetails(email));

        }

    }

    @GetMapping(value = "/users/sellersPaged")
    public ResponseEntity<?> getAllSellersPaged(HttpServletRequest request) {

        String email = request.getParameter("email");
        if(email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");


            return new ResponseEntity<>(userService.getAllSellersPaged(page, size, sort), HttpStatus.OK);
        }else{
            return ResponseEntity.ok(sellerService.fetchSellerProfileDetails(email));
        }

    }



    @PutMapping(value = "users/activate/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable(name = "id") Long sellerId) {

        String message;

        if (userService.activateAccount(sellerId)) {

            message = messageSource.getMessage("user.account.activated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK));

        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(),false,message,HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);


    }


    @PutMapping(value = "users/deactivate/{id}")
    public ResponseEntity<?> deActivateSeller(@PathVariable(name = "id") Long sellerId) {

        String message;

        if (userService.deActivateAccount(sellerId)){

            message = messageSource.getMessage("user.account.deactivated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true, message, HttpStatus.OK));

        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(),false,message,HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }

}
