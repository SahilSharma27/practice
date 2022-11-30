package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Address;

import java.util.List;

public interface CustomerService {

    boolean register(CustomerDTO customerDTO);

    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO) throws Exception;

    boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO);

    List<Address> getAllCustomerAddresses(String userEmail);

    boolean removeAddress(Long id);

    FetchCustomerDTO fetchCustomerProfileDetails(String userEmail);

    boolean updateProfile(String username, CustomerProfileDTO customerProfileDTO);

    List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId);
}
