package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.CustomerDTO;
import com.sahil.Ecom.entity.Customer;

public interface CustomerService {

    public Customer register(CustomerDTO customerDTO);
}
