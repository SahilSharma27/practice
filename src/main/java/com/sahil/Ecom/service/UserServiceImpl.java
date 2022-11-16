package com.sahil.Ecom.service;
import com.sahil.Ecom.controller.UserController;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.entity.UserAccessToken;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserAccessTokenRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        emailBody= "Activation Link: " + accessTokenService.generateURL(token);
        }catch (MalformedURLException e){
            logger.info("URL Error" + e);
            e.printStackTrace();
        }
//send email
        emailSenderService.sendEmail(email,"Account activation",emailBody);
    }

    @Override
    public String findEmailFromToken(String uuid) {
        UserAccessToken userAccessToken = accessTokenRepository.findByAccessToken(uuid);
        if(userAccessToken != null){
            return userAccessToken.getEmail();
        }
        else throw new UsernameNotFoundException("NO mail Found for token");
    }
}
