package com.sahil.ecom.service;

import com.sahil.ecom.dto.*;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;

import java.util.List;

public interface CustomerService {

    boolean register(AddCustomerDTO addCustomerDTO);

    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO) throws Exception;

    boolean addAddressToCustomer(AddAddressDTO addAddressDTO);

    List<FetchAddressDTO> getAllCustomerAddresses();

    void removeAddress(Long id);

    CustomerProfileDTO fetchCustomerProfileDetails();

    void updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO);

    List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId);
}
