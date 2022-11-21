package com.sahil.Ecom.controller;


import com.sahil.Ecom.entity.JwtRequest;
import com.sahil.Ecom.entity.JwtResponse;
import com.sahil.Ecom.helper.JwtUtil;
import com.sahil.Ecom.service.MyCustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class JwtController {

    @Autowired
    private MyCustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    Logger logger = LoggerFactory.getLogger(JwtController.class);

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        logger.info(jwtRequest.toString());

        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(), jwtRequest.getPassword()));

        } catch (UsernameNotFoundException e) {
            e.printStackTrace();

        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(jwtRequest.getUsername());

        String accessToken = this.jwtUtil.generateToken(userDetails);

        String refreshToken = accessToken;

        logger.info("TOKEN generated :" + accessToken);

        return ResponseEntity.ok(new JwtResponse(accessToken,refreshToken));
    }
}
