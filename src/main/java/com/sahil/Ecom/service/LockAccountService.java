package com.sahil.Ecom.service;


import com.sahil.Ecom.entity.LockedAccount;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.exception.AccountLockedException;
import com.sahil.Ecom.repository.LockedAccountRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
public class LockAccountService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(LockAccountService.class);

    @Autowired
    private LockedAccountRepository lockedAccountRepository;

    @Autowired
    private GeneralMailService generalMailService;

    @Transactional
    public void increaseCount(String userEmail){

        User user = userRepository.findByEmail(userEmail).get();


        if(user.getInvalidAttemptCount() >= 2) {
            lockAccount(user);
            return;
        }

        user.setInvalidAttemptCount(user.getInvalidAttemptCount()+1);
        logger.info("---------------------INCREASED COUNT---------------");

        userRepository.updateInvalidAttempts(user.getInvalidAttemptCount(),user.getEmail());
    }


    @Transactional
    private void lockAccount(User user){

        logger.info("---------------------COUNT REACHED 3---------------");
        user.setInvalidAttemptCount(user.getInvalidAttemptCount()+1);
        user.setLocked(true);

        LockedAccount lockedAccount = new LockedAccount();
        lockedAccount.setLockedTime(LocalDateTime.now());
        lockedAccount.setUser(user);

        user.setLockedAccount(lockedAccount);

        userRepository.save(user);
        logger.info("---------------------ACCOUNT LOCKED---------------");
        //send ack
        generalMailService.sendAccountLockedAck(user.getEmail());

    }

    @Transactional
    public boolean unlockWhenTimeExpired(User user) {

        if (user.getLockedAccount().getLockedTime().plusMinutes(1).isBefore(LocalDateTime.now())) {
            user.setLocked(false);
            user.setInvalidAttemptCount(0);
            user.setLockedAccount(null);
            lockedAccountRepository.deleteById(user.getId());
            userRepository.save(user);

            return true;
        }
        return false;

    }



}
