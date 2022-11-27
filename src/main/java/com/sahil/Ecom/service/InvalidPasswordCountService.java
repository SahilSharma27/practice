package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.exception.AccountLockedException;
import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class InvalidPasswordCountService {

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(InvalidPasswordCountService.class);




    public void increaseCount(String userEmail){

        User user = userRepository.findByEmail(userEmail).get();


        if(user.getInvalidAttemptCount() == 2) {
            user.setInvalidAttemptCount(user.getInvalidAttemptCount()+1);
            logger.info("---------------------COUNT REACHED 3---------------");
            lockAccount(user);
            //return;
        }

        user.setInvalidAttemptCount(user.getInvalidAttemptCount()+1);
        logger.info("---------------------INCREASED COUNT---------------");
        userRepository.save(user);
    }


    private void lockAccount(User user){

        user.setLocked(true);
        userRepository.save(user);
        logger.info("---------------------ACCOUNT LOCKED---------------");
        throw new AccountLockedException();
    }

}
