package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.FetchCustomerDTO;
import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.exception.CompanyNameAlreadyRegisteredException;
import com.sahil.Ecom.exception.EmailAlreadyRegisteredException;
import com.sahil.Ecom.exception.GstAlreadyRegisteredException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import io.jsonwebtoken.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
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

    Logger logger = LoggerFactory.getLogger(SellerController.class);

    @PostMapping(value = "/register",params = "role=seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody SellerDTO sellerDTO){

        Locale locale = LocaleContextHolder.getLocale();

        //unique email
        if(userService.checkUserEmail(sellerDTO.getEmail())){
            throw new EmailAlreadyRegisteredException(messageSource.getMessage("email.already.registered", null, "message", locale));
           // return new ResponseEntity<>("User email already registered", HttpStatus.BAD_REQUEST);
        }

        //unique company name
        if(sellerService.checkSellerCompanyName(sellerDTO.getCompanyName())){
            //return new ResponseEntity<>("Company name already exists",HttpStatus.BAD_REQUEST);
            throw new CompanyNameAlreadyRegisteredException(messageSource.getMessage("company.name.already.registered",null,"message",locale));
        }

        //unique gst
        if(sellerService.checkSellerGst(sellerDTO.getGst())){
//            return new ResponseEntity<>("Gst already exists",HttpStatus.BAD_REQUEST);
            throw new GstAlreadyRegisteredException(messageSource.getMessage("gst.already.registered",null,"message",locale));
        }


//      check pass and cpass
        if(!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException(messageSource.getMessage("password.confirmPassword", null, "message", locale));
           // return new ResponseEntity<>("Password and confirm password doesn't match",HttpStatus.BAD_REQUEST);

//        1)save user
        sellerService.register(sellerDTO);

        //send acknowledgment
       // userService.sendSellerAcknowledgement(sellerDTO.getEmail());


        ResponseDTO responseDTO =  new ResponseDTO();
        responseDTO.setTimestamp(new Date());

        responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));
//        responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));

        responseDTO.setResponseStatusCode(200);
        return ResponseEntity.ok(responseDTO);
        //return new ResponseEntity<>("User registered Successfully", HttpStatus.OK);

    }


    @GetMapping(value = "users/profile", params = "role=seller")
    public ResponseEntity<?> getCustomerProfile(HttpServletRequest request) {
        //get token
        //get username
        //get profile

        String requestHeader = request.getHeader("Authorization");

        String userEmail = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer".length());

            try {
                userEmail = this.jwtUtil.extractUsername(accessToken);
                FetchSellerDTO fetchSellerDTO =  sellerService.fetchSellerProfileDetails(userEmail);
                return ResponseEntity.ok(fetchSellerDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("", HttpStatus.NOT_FOUND);

    }



}
