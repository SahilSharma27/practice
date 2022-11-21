package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ActivateSellerDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<Iterable<User>>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping(value = "/users/customers")
    public ResponseEntity<?> getAllCustomers(){
        return new ResponseEntity<Iterable<Customer>>(userService.getAllCustomers(),HttpStatus.OK);

    }

    @GetMapping(value = "/users/sellers")
    public ResponseEntity<?>getAllSellers(){
        return new ResponseEntity<Iterable<Seller>>(userService.getAllSellers(),HttpStatus.OK);
    }


    @GetMapping(value = "users/sellers/activate/{id}")
    public ResponseEntity<?> activateSeller(@PathVariable(name = "id") Long sellerId){

        //find by id
        //update is_active to true;
            if(userService.activateAccount(sellerId))
                return new ResponseEntity<>("SELLER Account Activated", HttpStatus.OK);

            return new ResponseEntity<>("SELLER Account NOT FOUND", HttpStatus.NOT_FOUND);

        //        if(sellerService.checkUserEmail(activateSellerDTO.getUserEmail())){
//            userService.activate(activateSellerDTO.getUserEmail());
//            return new ResponseEntity<>("SELLER Account Activated", HttpStatus.OK);
//        }

    }

    @PutMapping(value = "users/customers/activate")
    public ResponseEntity<?> activateCustomer(@RequestBody ActivateSellerDTO activateSellerDTO){

//        if(userService.checkUserEmail(activateSellerDTO.getUserEmail())){
//            userService.activate(activateSellerDTO.getUserEmail());
//            return new ResponseEntity<>("Customer Account Activated", HttpStatus.OK);
//        }
        return new ResponseEntity<>("Customer Account NOT FOUND", HttpStatus.NOT_FOUND);

    }

    @PutMapping(value = "users/sellers/deactivate/{id}")
    public ResponseEntity<?> deActivateSeller(@PathVariable Long sellerId){

        if(userService.deActivateAccount(sellerId))
            return new ResponseEntity<>("SELLER Account De Activated", HttpStatus.OK);

        return new ResponseEntity<>("SELLER Account NOT FOUND", HttpStatus.NOT_FOUND);
    }


    @PutMapping(value = "users/customers/deactivate")
    public ResponseEntity<?> deActivateCustomer(@RequestBody ActivateSellerDTO activateSellerDTO){

//        if(userService.checkUserEmail(activateSellerDTO.getUserEmail())){
//            userService.activate(activateSellerDTO.getUserEmail());
//            return new ResponseEntity<>("SELLER Account Activated", HttpStatus.OK);
//        }

        return new ResponseEntity<>("SELLER Account NOT FOUND", HttpStatus.NOT_FOUND);

    }

}
