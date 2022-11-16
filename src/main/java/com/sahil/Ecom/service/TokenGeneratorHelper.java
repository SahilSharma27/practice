package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.JwtRequest;
import com.sahil.Ecom.entity.JwtResponse;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.helper.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class TokenGeneratorHelper {

    @Autowired
    private MyCustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;


    public String generateTokenHelper(JwtRequest jwtRequest) throws Exception{

        try{
            this.authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    jwtRequest.getUsername()
                                    ,jwtRequest.getPassword()
                            )
                    );
        }catch(UsernameNotFoundException e){
            e.printStackTrace();

        }catch(BadCredentialsException e){
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails =  customUserDetailsService
                .loadUserByUsername(jwtRequest.getUsername());

        return this.jwtUtil.generateToken(userDetails);

//        logger.info("TOKEN generated :" + token);

//        return ResponseEntity.ok(new JwtResponse(token));

    }
}
