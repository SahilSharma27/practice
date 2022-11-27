package com.sahil.Ecom.service;

import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationEventListener {
    @Autowired
    InvalidPasswordCountService invalidPasswordCountService;

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(InvalidPasswordCountService.class);

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        logger.info("---------------------INSIDE BAD CREDENTIAL LISTENER---------------");

        String username = (String) event.getAuthentication().getPrincipal();
        logger.info(username);

        if(userRepository.existsByEmail(username)){
            logger.info("---------------------USER NAME EXIST PASSWORD WRONG---------------");
                invalidPasswordCountService.increaseCount(username);
                return;
        }

        logger.info("---------------------USER NAME DOESN'T EXIST---------------");


    }
}
