package com.sahil.Ecom.security;

import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.repository.UserRepository;
import com.sahil.Ecom.service.LockAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationEventListener {
    @Autowired
    LockAccountService lockAccountService;

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(LockAccountService.class);

    @EventListener
    public void authenticationFailed(AuthenticationFailureBadCredentialsEvent event) {

        logger.info("---------------------INSIDE BAD CREDENTIAL LISTENER---------------");

        String username = (String) event.getAuthentication().getPrincipal();
        logger.info(username);

        if(userRepository.existsByEmail(username)){
            logger.info("---------------------USER NAME EXIST PASSWORD WRONG---------------");

            logger.info("CHECKING FOR ADMIN");

            User user = userRepository.findByEmail(username).get();
            if(user.getRoles().get(0).getAuthority().equals("ROLE_ADMIN")){
                return;
            }
                lockAccountService.increaseCount(username);
                return;
        }

        logger.info("---------------------USER NAME DOESN'T EXIST---------------");


    }
}
