package com.sahil.Ecom.security;

import com.sahil.Ecom.entity.JwtRequest;
import com.sahil.Ecom.entity.JwtResponse;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.MyCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TokenGeneratorHelper {

    @Autowired
    private MyCustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    MessageSource messageSource;

    public JwtResponse generateTokenHelper(JwtRequest jwtRequest) throws Exception{

        Locale locale = LocaleContextHolder.getLocale();
        try{
            this.authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    jwtRequest.getUsername()
                                    ,jwtRequest.getPassword()
                            )
                    );
        }catch(UsernameNotFoundException | BadCredentialsException e){
//            e.printStackTrace();
            throw new BadCredentialsException(messageSource.getMessage("user.login.bad.credentials", null, "message", locale));

        }

        UserDetails userDetails =  customUserDetailsService
                .loadUserByUsername(jwtRequest.getUsername());


        return new JwtResponse(this.jwtUtil.generateToken(userDetails),this.jwtUtil.generateRefreshToken(userDetails));

//        logger.info("TOKEN generated :" + token);

//        return ResponseEntity.ok(new JwtResponse(token));

    }
}
