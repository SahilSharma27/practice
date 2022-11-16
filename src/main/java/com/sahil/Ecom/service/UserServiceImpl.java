package com.sahil.Ecom.service;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


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
    public User activate(Long id) {
        User foundUser = userRepository.findById(id).orElse(null);

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
}
