package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ForgotPasswordDTO;
import com.sahil.Ecom.dto.RegisterDTO;
import com.sahil.Ecom.dto.ResetPassDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.service.AccessTokenService;
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

import java.net.URL;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    TokenGeneratorHelper tokenGeneratorHelper;


    Logger logger = LoggerFactory.getLogger(UserController.class);

//    @GetMapping(value = "/users/customers")
//    public Iterable<Customer> getAllCustomers(){return userService.getAllCustomers();
//    }
//
//    @GetMapping(value = "/users/sellers")
//    public Iterable<Seller> getAllSellers(){
//        return userService.getAllSellers();
//    }
//
//    @GetMapping(value = "/users",params = "role=admin")
//    public Iterable<User> getAllUsers(){
//        return userService.getAllUsers();
//    }

    @PostMapping(value = "/register",params = "role=customer")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) throws Exception {

//      Check if email taken
        if(userService.checkUserEmail(customer.getEmail())){
            return new ResponseEntity<>("User email already registered", HttpStatus.BAD_REQUEST);
        }

//        1)save user
        userService.register(customer);

//        2)send activation link
        userService.activationHelper(customer.getEmail());

        return new ResponseEntity<>("User registered Successfully", HttpStatus.OK);
    }

    @PostMapping(value = "/register",params = "role=seller")
    public Seller registerSeller(@RequestBody Seller seller){
        return userService.register(seller);
    }



    @PostMapping(value = "/login",params = "role=admin")
    public ResponseEntity<?> loginAdmin(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/login",params = "role=customer")
    public ResponseEntity<?> loginCustomer(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/login",params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody JwtRequest jwtRequest) throws Exception {

        String token  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

        logger.info("token : " + token);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @GetMapping(value = "/users/activate")
    public ResponseEntity<String> activateAccount(@RequestParam(name="token") String uuid){

//        1 check token in db
//        2 get email
//        3 update account is active

        String email = userService.findEmailFromAccessToken(uuid);

        userService.activate(email);

        return new ResponseEntity<>("USER ACCOUNT ACTIVATED",HttpStatus.OK);

    }


    @PostMapping(value = "/users/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO){

//        1 check Email in db
//        2 generate url
//        3 send mail
        if(userService.checkUserEmail(forgotPasswordDTO.getEmail())){


            userService.forgotPasswordHelper(forgotPasswordDTO.getEmail());
            return new ResponseEntity<>("Reset Password link Sent", HttpStatus.OK);


        }

//        2)send activation link


        return new ResponseEntity<>("USER ACCOUNT Not found",HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/users/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String token,@RequestBody ResetPassDTO resetPassDTO){
        logger.info("------------------------------------------------------------------");
        logger.info(resetPassDTO.getNewPassword() +"  "+resetPassDTO.getConfirmNewPassword());
        logger.info("------------------------------------------------------------------");

        if(resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())){
            String email = userService.findEmailFromResetPassToken(token);

            if(email.equals(resetPassDTO.getUserEmail())){
                userService.resetPassword(email,resetPassDTO.getNewPassword());
            }

            return new ResponseEntity<>("User Password Updated",HttpStatus.OK);
        }

        //        1 check token in db
//        2 get email
//        3 update account pass

        return new ResponseEntity<>("Password doesn't match with confirm password",HttpStatus.BAD_REQUEST);



    }




}
