package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.exception.*;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.service.LoginService;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;


@RestController
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private SellerService sellerService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LoginService loginService;

    Logger logger = LoggerFactory.getLogger(SellerController.class);

    Locale locale = LocaleContextHolder.getLocale();

    @PostMapping(value = "/register", params = "role=seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody SellerDTO sellerDTO) {

        Locale locale = LocaleContextHolder.getLocale();

        //check pass and cpass
        if (!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException();

        //unique email
        if (userService.checkUserEmail(sellerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        //unique company name
        if (sellerService.checkSellerCompanyName(sellerDTO.getCompanyName())) {
            throw new CompanyNameAlreadyRegisteredException();
        }

        //unique gst
        if (sellerService.checkSellerGst(sellerDTO.getGst())) {
            throw new GstAlreadyRegisteredException();
        }

//        1)save user
        if (sellerService.register(sellerDTO)) {

            //send acknowledgment
             userService.sendSellerAcknowledgement(sellerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));

            return ResponseEntity.ok(responseDTO);

        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping(value = "/login", params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {


        LoginResponseDTO loginResponseDTO = sellerService.loginSeller(loginRequestDTO);
        if(loginResponseDTO!=null){
            return ResponseEntity.ok(loginResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(),false,HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("login.failed",null,"message",locale));
        return new ResponseEntity<>(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @GetMapping(value = "users/profile", params = "role=seller")
    public ResponseEntity<?> getSellerProfile(HttpServletRequest request) {
        //get token
        //get username
        //get profile

        String requestHeader = request.getHeader("Authorization");

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer ".length());

            try {

                String userEmail = this.jwtUtil.extractUsername(accessToken);
                return ResponseEntity.ok(sellerService.fetchSellerProfileDetails(userEmail));

            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }
        }
        throw new InvalidTokenException();

    }

    @PatchMapping(value = "/users/update/profile" ,params = "role=seller")
    public ResponseEntity<?> updateProfile(@RequestBody SellerProfileUpdateDTO sellerProfileUpdateDTO, HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            username = jwtUtil.extractUsername(accessToken);

            sellerService.updateSellerProfile(username,sellerProfileUpdateDTO);
            String message = messageSource.getMessage("user.profile.updated",null,"message",locale);

            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK));
        }

        throw new InvalidTokenException();
    }



}
