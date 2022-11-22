package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.FetchCustomerDTO;
import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;

import java.util.List;

public interface UserService {

    public Customer login(Customer customer);

    public Seller login(Seller seller);

    User activateByEmail(String email);

    public boolean activateAccount(Long id);

    public boolean deActivateAccount(Long id);

    public Iterable<User> getAllUsers();

    public List<FetchCustomerDTO> getAllCustomers();

    public List<FetchSellerDTO> getAllSellers();

    public boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String validateActivationToken(String uuid);

    void forgotPasswordHelper(String email);

    String validateResetPasswordToken(String uuid);


    void resetPassword(String email,String newPassword);

    void sendSellerAcknowledgement(String email);


    boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO);

    List<Address> getAllCustomerAddresses(String userEmail);

    boolean removeAddress(Long id);
}
