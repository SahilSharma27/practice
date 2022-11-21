package com.sahil.Ecom.service;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.TokenExpiredException;
import com.sahil.Ecom.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    private UUIDTokenService UUIDTokenService;

    @Autowired
    private ResetPassTokenRepository resetPassTokenRepository;


    @Autowired
    SellerService sellerService;

    @Autowired
    MessageSource messageSource;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);




    @Override
    public Customer login(Customer customer) {
        return null;
    }

    @Override
    public Seller login(Seller seller) {
        return null;
    }

    @Override
    public User activateByEmail(String email) {

        User foundUser = userRepository.findByEmail(email).orElse(null);

        if(foundUser !=null && !foundUser.isActive()){

            foundUser.setActive(true);
            return userRepository.save(foundUser);

        }else {
            throw  new UsernameNotFoundException("Not found");
        }
    }

    @Override
    public boolean activateAccount(Long id) {

        if(userRepository.existsById(id)){
            User foundUser = userRepository.findById(id).orElse(null);

            if(foundUser !=null && !foundUser.isActive()){

                foundUser.setActive(true);
                userRepository.save(foundUser);
                return true;

            }

        }
        return false;
    }

    @Override
    public boolean deActivateAccount(Long id) {

        if(userRepository.existsById(id)){
            User foundUser = userRepository.findById(id).orElse(null);

            if(foundUser !=null && foundUser.isActive()){

                foundUser.setActive(false);

                logger.info("---------------ACCOUNT DEACTIVATED-------------------");

                userRepository.save(foundUser);
                return true;

            }

        }

        return false;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Iterable<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public boolean checkUserEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void activationHelper(String email) {
//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ActivationToken activationToken =  new ActivationToken();
        activationToken.setActivationToken(token);
        activationToken.setUserEmail(email);

        // setting time limits to the token
        LocalDateTime currentDateTime =  LocalDateTime.now();
        activationToken.setTokenTimeLimit(currentDateTime.plusMinutes(1));

        activationTokenRepository.save(activationToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
        emailBody= "Activation Link: " + UUIDTokenService.generateActivationURL(token);
        }catch (MalformedURLException e){
            logger.info("URL Error" + e);
            e.printStackTrace();
        }

        logger.info("ACTIVATION URL" + emailBody);

        //send email
       // emailSenderService.sendEmail(email,"Account activation",emailBody);
    }

    @Override
    public String validateActivationToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ActivationToken activationToken = activationTokenRepository.findByActivationToken(uuid);

        //check expiration
        //return email id if all good
        if(activationToken != null){
            if(activationToken.getTokenTimeLimit().isBefore(LocalDateTime.now())){

                logger.info("-------------------TIME LIMIT EXCEEDED---------------");

                //remove token from table
                //send new url if time limit exceeds and throw exception

                activationTokenRepository.deleteById(uuid);

                activationHelper(activationToken.getUserEmail());

                throw new TokenExpiredException(messageSource.getMessage("token.expired", null, "message", locale));

            }

            logger.info("-------------------UNDER TIME LIMIT---------------");
            return activationToken.getUserEmail();
        }

        else throw new UsernameNotFoundException("NO mail Found for token");
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User foundUser = userRepository.findByEmail(email).orElse(null);

        if(foundUser !=null && foundUser.isActive()){

            foundUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(foundUser);

        }else {
            throw  new UsernameNotFoundException("Not found");
        }

    }

    public String validateResetPasswordToken(String uuid) {

        Locale locale = LocaleContextHolder.getLocale();

        //find token in table
        ResetPasswordToken resetPasswordToken = resetPassTokenRepository.findByResetPassToken(uuid);

        //check expiration
        //return email id if all good
        if(resetPasswordToken != null){

            if(resetPasswordToken.getTokenTimeLimit().isBefore(LocalDateTime.now())){

                logger.info("-------------------TIME LIMIT EXCEEDED---------------");

                //remove token from table
                //send new url if time limit exceeds and throw exception

                resetPassTokenRepository.deleteById(uuid);

//                activationHelper(userAccessToken.getEmail());

                throw new TokenExpiredException(messageSource.getMessage("token.expired", null, "message", locale));

            }

            logger.info("-------------------UNDER TIME LIMIT---------------");
            return resetPasswordToken.getUserEmail();
        }


        logger.info("-------------------UNDER TIME LIMIT STILL HERE--------------");
        throw new UsernameNotFoundException("NO mail Found for token");

    }

    @Override
    public void forgotPasswordHelper(String email) {
//        generate token
        String token = UUIDTokenService.getUUIDToken();

//        save in db
        ResetPasswordToken resetPasswordToken =  new ResetPasswordToken();
        resetPasswordToken.setResetPassToken(token);
        resetPasswordToken.setUserEmail(email);
        resetPasswordToken.setTokenTimeLimit(LocalDateTime.now().plusMinutes(1));
        resetPassTokenRepository.save(resetPasswordToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
            emailBody= "Reset Password Link: " + UUIDTokenService.generateResetPassURL(token);
        }catch (MalformedURLException e){
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
//send email
       // emailSenderService.sendEmail(email,"Reset Password",emailBody);
        logger.info(emailBody);
    }

    @Override
    public void sendSellerAcknowledgement(String email) {

        String emailBody = "ACCOUNT CREATED WAITING FOR APPROVAL" ;
        emailSenderService.sendEmail(email,"Registration Successfully",emailBody);

    }


}
