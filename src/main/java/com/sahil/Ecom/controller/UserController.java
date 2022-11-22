package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ForgotPasswordDTO;
import com.sahil.Ecom.dto.ResetPassDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.LoginService;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    MessageSource messageSource;


    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    JwtUtil jwtUtil;


    @Autowired
    LoginService loginService;



    Locale locale = LocaleContextHolder.getLocale();

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtRefreshToken = null;
        String newAccessToken = null;


        //check format
        if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer")) {
            jwtRefreshToken = refreshTokenHeader.substring("Bearer".length());

            try{
                username = this.jwtUtil.extractUsername(jwtRefreshToken);
            }catch (Exception e) {
                e.printStackTrace();
            }
//
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            newAccessToken = this.jwtUtil.generateToken(userDetails);

            logger.info("------------------REFRESH________________");
            logger.info(newAccessToken);

            }

        return new ResponseEntity<>(new JwtResponse(newAccessToken,jwtRefreshToken),HttpStatus.OK);
    }


    @PostMapping(value = "/login",params = "role=admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody JwtRequest jwtRequest) throws Exception {

        //remove all tokens in db for this user
        //generate new tokens
        //save new tokens in db

//        loginService.removeAlreadyGeneratedTokens(jwtRequest);

        JwtResponse jwtResponse = tokenGeneratorHelper.generateTokenHelper(jwtRequest);

//        loginService.saveJwtResponse(jwtResponse,jwtRequest.getUsername());

//        logger.info("token : " + jwtResponse);

        return ResponseEntity.ok(jwtResponse);
    }


//    @PostMapping(value = "/login",params = "role=customer")
//    public ResponseEntity<?> loginCustomer(@RequestBody JwtRequest jwtRequest) throws Exception {
//
//        String accessToken  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);
//
//        logger.info("accessToken : " + accessToken);
//
//        return ResponseEntity.ok(new JwtResponse(accessToken,accessToken));
//    }
//
//    @PostMapping(value = "/login",params = "role=seller")
//    public ResponseEntity<?> loginSeller(@RequestBody JwtRequest jwtRequest) throws Exception {
//
//        String accessToken  = tokenGeneratorHelper.generateTokenHelper(jwtRequest);
//
//        logger.info("token : " + accessToken);
//
//        return ResponseEntity.ok(new JwtResponse(accessToken,accessToken));
//    }

    @GetMapping(value = "/users/activate")
    public ResponseEntity<String> activateAccount(@RequestParam(name="token") String uuid){

//        1 check token in db
//        2 check time limit
//        3 get email
//        4 update account is active

        String email = userService.validateActivationToken(uuid);

        userService.activateByEmail(email);

        return new ResponseEntity<>("USER ACCOUNT ACTIVATED",HttpStatus.OK);

    }


    @PostMapping(value = "/users/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO){

//        1 check Email in db
//        2 generate url
//        3 send mail

        if(userService.checkUserEmail(forgotPasswordDTO.getUserEmail())){

            userService.forgotPasswordHelper(forgotPasswordDTO.getUserEmail());
            return new ResponseEntity<>("Reset Password link Sent", HttpStatus.OK);

        }

//        2)send reset pass link link

        throw new UserEmailNotFoundException(messageSource.getMessage("user.not.found", null, "message", locale));
        //return new ResponseEntity<>("USER ACCOUNT Not found",HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/users/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String token,@RequestBody ResetPassDTO resetPassDTO){
//        1 check token in db
//        check time limit
//        2 get email
//        3 update account pass

        logger.info("------------------------------------------------------------------");
        logger.info(resetPassDTO.getNewPassword() +"  "+resetPassDTO.getConfirmNewPassword());
        logger.info("------------------------------------------------------------------");

        if(resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())){
            String email = userService.validateResetPasswordToken(token);

            if(email.equals(resetPassDTO.getUserEmail())){
                userService.resetPassword(email,resetPassDTO.getNewPassword());
            }
            return ResponseEntity.ok(new ResponseDTO(new Date(),"User Password Updated",200));
        }

        throw new PassConfirmPassNotMatchingException(messageSource.getMessage("password.confirmPassword", null, "message", locale));
//        return new ResponseEntity<>("Password doesn't match with confirm password",HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = "/users/logout")
    public ResponseEntity<?> logoutUSer(@RequestParam String token) {

        return new ResponseEntity<>("HELLO",HttpStatus.OK);
    }

//



}
