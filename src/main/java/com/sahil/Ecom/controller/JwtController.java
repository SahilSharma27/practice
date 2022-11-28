package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.LoginRequestDTO;
import com.sahil.Ecom.dto.LoginResponseDTO;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.MyCustomUserDetailsService;
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
    public ResponseEntity<?> getToken(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {
        logger.info(loginRequestDTO.toString());

        try {
            this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        } catch (UsernameNotFoundException e) {
            e.printStackTrace();

        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("Bad Credentials");
        }

        UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequestDTO.getUsername());

        String accessToken = this.jwtUtil.generateToken(userDetails);

        String refreshToken = accessToken;

        logger.info("TOKEN generated :" + accessToken);

        return ResponseEntity.ok(new LoginResponseDTO(accessToken,refreshToken));
    }
}
