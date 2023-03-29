package com.sahil.ecom.service;

import com.sahil.ecom.dto.*;
import com.sahil.ecom.dto.address.AddAddressDTO;
import com.sahil.ecom.dto.address.FetchAddressDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;

import java.util.List;

public interface CustomerService {

    boolean register(AddCustomerDTO addCustomerDTO);

    LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO);

    boolean addAddressToCustomer(AddAddressDTO addAddressDTO);

    List<FetchAddressDTO> getAllCustomerAddresses();

    boolean removeAddress(Long id);

    CustomerProfileDTO fetchCustomerProfileDetails();

    boolean updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO);

    List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId);
}
