package com.sahil.Ecom.service;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserAccessTokenRepository accessTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private ResetUserPassRepository resetUserPassRepository;

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public Customer register(Customer customer) {

        String password = customer.getPassword();
        customer.setPassword(passwordEncoder.encode(password));
        return customerRepository.save(customer);
    }

    @Override
    public Seller register(Seller seller) {
        String password = seller.getPassword();
        seller.setPassword(passwordEncoder.encode(password));
        return sellerRepository.save(seller);
    }

    @Override
    public Customer login(Customer customer) {
        return null;
    }

    @Override
    public Seller login(Seller seller) {
        return null;
    }


    @Override
    public User activate(String email) {
        User foundUser = userRepository.findByEmail(email).orElse(null);

        if(foundUser !=null && !foundUser.isActive()){

            foundUser.setActive(true);
            return userRepository.save(foundUser);

        }else {
            throw  new UsernameNotFoundException("Not found");
        }

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
        String token = accessTokenService.getAccessToken();

//        save in db
        UserAccessToken userAccessToken =  new UserAccessToken();
        userAccessToken.setAccessToken(token);
        userAccessToken.setEmail(email);
        accessTokenRepository.save(userAccessToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
        emailBody= "Activation Link: " + accessTokenService.generateActivationURL(token);
        }catch (MalformedURLException e){
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
//send email
        emailSenderService.sendEmail(email,"Account activation",emailBody);
    }

    @Override
    public String findEmailFromAccessToken(String uuid) {
        UserAccessToken userAccessToken = accessTokenRepository.findByAccessToken(uuid);
        if(userAccessToken != null){
            return userAccessToken.getEmail();
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

    @Override
    public String findEmailFromResetPassToken(String uuid) {
        ResetPassToken resetPassToken = resetUserPassRepository.findByResetPassToken(uuid);
        if(resetPassToken != null){
            return resetPassToken.getEmail();
        }
        else throw new UsernameNotFoundException("NO mail Found for token");

    }

    @Override
    public void forgotPasswordHelper(String email) {
//        generate token
        String token = accessTokenService.getAccessToken();

//        save in db
        ResetPassToken resetPassToken =  new ResetPassToken();
        resetPassToken.setResetPassToken(token);
        resetPassToken.setEmail(email);
        resetUserPassRepository.save(resetPassToken);

        logger.info("UUID as String: " + token);

        //generate url
        String emailBody = "";
        try {
            emailBody= "Reset Password Link: " + accessTokenService.generateResetPassURL(token);
        }catch (MalformedURLException e){
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
//send email
        emailSenderService.sendEmail(email,"Reset Password",emailBody);
    }
}
