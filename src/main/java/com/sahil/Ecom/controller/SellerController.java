package com.sahil.Ecom.controller;

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

    Logger logger = LoggerFactory.getLogger(SellerController.class);

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
            // userService.sendSellerAcknowledgement(sellerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));

            return ResponseEntity.ok(responseDTO);

        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

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
