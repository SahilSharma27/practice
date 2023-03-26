package com.sahil.ecom.service.impl;


import com.sahil.ecom.entity.LockedAccount;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.LockedAccountRepository;
import com.sahil.ecom.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
@Slf4j
public class LockAccountService {
    private final UserRepository userRepository;
    private final LockedAccountRepository lockedAccountRepository;
    private GeneralMailService generalMailService;

    @Transactional
    public void increaseCount(String userEmail) {

        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new GenericException(""));


        if (user.getInvalidAttemptCount() >= 2) {
            lockAccount(user);
            return;
        }

        user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);
        log.info("---------------------INCREASED COUNT---------------");

        userRepository.updateInvalidAttempts(user.getInvalidAttemptCount(), user.getEmail());
    }


    @Transactional
    public void lockAccount(User user) {

        log.info("---------------------COUNT REACHED 3---------------");
        user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);
        user.setLocked(true);

        LockedAccount lockedAccount = new LockedAccount();
        lockedAccount.setLockedTime(LocalDateTime.now());
        lockedAccount.setUser(user);

        user.setLockedAccount(lockedAccount);

        userRepository.save(user);
        log.info("---------------------ACCOUNT LOCKED---------------");
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
