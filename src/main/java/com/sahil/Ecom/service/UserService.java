package com.sahil.Ecom.service;


import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;

public interface UserService {

    public Customer register(Customer customer);

    public Seller register(Seller seller);

    public Iterable<User> getAllUsers();

    public Iterable<Customer> getAllCustomers();

    public Iterable<Seller> getAllSellers();






}
