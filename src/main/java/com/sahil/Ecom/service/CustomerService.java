package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.dto.category.FetchCategoryDTO;
import com.sahil.Ecom.dto.customer.CustomerDTO;
import com.sahil.Ecom.dto.customer.CustomerProfileDTO;
import com.sahil.Ecom.dto.customer.CustomerProfileUpdateDTO;

import java.util.List;

public interface CustomerService {

    boolean register(CustomerDTO customerDTO);

    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO) throws Exception;

    boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO);

    List<AddressDTO> getAllCustomerAddresses(String userEmail);

    void removeAddress(Long id,String userEmail);

    CustomerProfileDTO fetchCustomerProfileDetails(String userEmail);

    void updateProfile(String username, CustomerProfileUpdateDTO customerProfileUpdateDTO);

    List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId);
}
