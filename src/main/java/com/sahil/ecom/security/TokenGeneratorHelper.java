package com.sahil.ecom.security;

import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.exception.GenericException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
@Slf4j
public class TokenGeneratorHelper {


    private final MessageSource messageSource;

    private final MyCustomUserDetailsService customUserDetailsService;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public LoginResponseDTO generateTokenHelper(LoginRequestDTO loginRequestDTO) {

        Locale locale = LocaleContextHolder.getLocale();
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        } catch (BadCredentialsException e) {

            log.info("------------------------BAD CREDENTIAL EXCEPTION THROWN --------------------");
            throw new BadCredentialsException(messageSource.getMessage("user.login.bad.credentials", null, "message", locale));

        } catch (DisabledException e) {
            throw new GenericException("Account Not Active");
        } catch (LockedException e) {
            throw new GenericException("Account Locked ");
        }


        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequestDTO.getUsername());


        return new LoginResponseDTO(this.jwtUtil.generateToken(userDetails), this.jwtUtil.generateRefreshToken(userDetails));


    }
}
