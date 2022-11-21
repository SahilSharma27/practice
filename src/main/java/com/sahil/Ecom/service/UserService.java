package com.sahil.Ecom.service;


import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;

public interface UserService {


    public Customer login(Customer customer);

    public Seller login(Seller seller);

    User activateByEmail(String email);

    public boolean activateAccount(Long id);

    public boolean deActivateAccount(Long id);

    public Iterable<User> getAllUsers();

    public Iterable<Customer> getAllCustomers();

    public Iterable<Seller> getAllSellers();

    public boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String validateActivationToken(String uuid);

    void forgotPasswordHelper(String email);

    String validateResetPasswordToken(String uuid);


    void resetPassword(String email,String newPassword);

    void sendSellerAcknowledgement(String email);



}
