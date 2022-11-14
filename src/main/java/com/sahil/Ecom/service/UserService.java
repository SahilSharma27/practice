package com.sahil.Ecom.service;


import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;

public interface UserService {

    public User register(User user,String role);

    public Iterable<User> getAllUsers();

    public Iterable<Customer> getAllCustomers();

    public Iterable<Seller> getAllSellers();






}
