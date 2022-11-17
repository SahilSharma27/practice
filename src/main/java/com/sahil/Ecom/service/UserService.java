package com.sahil.Ecom.service;


import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;

public interface UserService {


    public Customer login(Customer customer);

    public Seller login(Seller seller);

    public Customer register(Customer customer);

    public Seller register(Seller seller);

    public User activate(String email);

    public Iterable<User> getAllUsers();

    public Iterable<Customer> getAllCustomers();

    public Iterable<Seller> getAllSellers();

    public boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String findEmailFromAccessToken(String uuid);

    void forgotPasswordHelper(String email);

    String findEmailFromResetPassToken(String uuid);


    void resetPassword(String email,String newPassword);

    void sendSellerAcknowledgement(String email);
}
