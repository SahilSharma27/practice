package com.sahil.Ecom.security;

import com.sahil.Ecom.entity.LoginRequestDTO;
import com.sahil.Ecom.entity.LoginResponseDTO;
import com.sahil.Ecom.exception.AccountLockedException;
import com.sahil.Ecom.exception.AccountNotActiveException;
import com.sahil.Ecom.repository.UserRepository;
import com.sahil.Ecom.service.LockAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
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
    UserRepository userRepository;





    Logger logger = LoggerFactory.getLogger(TokenGeneratorHelper.class);

    @Autowired
    MessageSource messageSource;

    public LoginResponseDTO generateTokenHelper(LoginRequestDTO loginRequestDTO) throws Exception{

        Locale locale = LocaleContextHolder.getLocale();
        try {
            this.authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequestDTO.getUsername()
                                    , loginRequestDTO.getPassword()
                            )
                    );
        }catch( BadCredentialsException e){

            logger.info("------------------------BAD CREDENTIAL EXCEPTION THROWN --------------------");
            throw new BadCredentialsException(messageSource.getMessage("user.login.bad.credentials", null, "message", locale));

        }catch (DisabledException e){

            throw new AccountNotActiveException();

        }catch (LockedException e){


            throw new AccountLockedException();
        }


        UserDetails userDetails =  customUserDetailsService
                .loadUserByUsername(loginRequestDTO.getUsername());


        return new LoginResponseDTO(this.jwtUtil.generateToken(userDetails),this.jwtUtil.generateRefreshToken(userDetails));



    }
}
