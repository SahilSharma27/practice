package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.CustomerDTO;
import com.sahil.Ecom.dto.FetchCustomerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CustomerService {

    public Customer register(CustomerDTO customerDTO);

    boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO);

    List<Address> getAllCustomerAddresses(String userEmail);

    boolean removeAddress(Long id);

    FetchCustomerDTO fetchCustomerProfileDetails(String userEmail);
}
