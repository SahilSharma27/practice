package com.sahil.ecom.service.impl;


import com.sahil.ecom.dto.AddAddressDTO;
import com.sahil.ecom.dto.FetchAddressDTO;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.ecom.entity.Address;
import com.sahil.ecom.entity.Customer;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.AddressRepository;
import com.sahil.ecom.repository.CustomerRepository;
import com.sahil.ecom.repository.RoleRepository;
import com.sahil.ecom.repository.UserRepository;
import com.sahil.ecom.security.AuthUserService;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.CategoryService;
import com.sahil.ecom.service.CustomerService;
import com.sahil.ecom.service.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final LoginService loginService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final LockAccountService lockAccountService;
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    private final AuthUserService authUserService;

    @Override
    public boolean register(AddCustomerDTO addCustomerDTO) {

        Customer newCustomer = new Customer();

        newCustomer.setEmail(addCustomerDTO.getEmail());
        newCustomer.setFirstName(addCustomerDTO.getFirstName());
        newCustomer.setMiddleName(addCustomerDTO.getMiddleName());
        newCustomer.setLastName(addCustomerDTO.getLastName());

        newCustomer.setPassword(passwordEncoder.encode(addCustomerDTO.getPassword()));
        newCustomer.setContact(addCustomerDTO.getContact());

        newCustomer.setRoles(Collections.singletonList(roleRepository.findByAuthority("ROLE_CUSTOMER")));

        newCustomer.setActive(false);
        newCustomer.setDeleted(false);
        newCustomer.setExpired(false);
        newCustomer.setLocked(false);
        newCustomer.setPasswordUpdateDate(new Date());
        newCustomer.setInvalidAttemptCount(0);


        customerRepository.save(newCustomer);
        return true;

    }

    @Override
    public LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO) throws Exception {

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return loginResponseDTO;


    }


    @Override
    public boolean addAddressToCustomer(AddAddressDTO addAddressDTO) {

        User user = authUserService.getCurrentAuthorizedUser();
//        User user = userRepository.findByEmail(userEmail).orElseThrow(GenericException::new);

        //check address already exist
        user.getAddresses().forEach(address -> {
            if (address.getLabel().equalsIgnoreCase(addAddressDTO.getLabel())) {
                throw new GenericException(messageSource.getMessage("same.address.exist", null, "message", LocaleContextHolder.getLocale()));
            }
        });

        Address newAddress = addAddressDTO.mapAddressDTOtoAddress();
        user.getAddresses().add(newAddress);

        userRepository.save(user);
        return true;

    }

    @Override
    public List<FetchAddressDTO> getAllCustomerAddresses() {
        User user = authUserService.getCurrentAuthorizedUser();
        return user.getAddresses().stream().map(FetchAddressDTO::new).toList();
    }

    @Override
    public void removeAddress(Long id) {
        User user = authUserService.getCurrentAuthorizedUser();
//        user.getAddresses().forEach(address ->{
//            if(address.getId().equals(id)){
//                addressRepository.deleteById(id);
//                return;
//            }
//        });

        user.getAddresses().stream().filter(address -> address.getId().equals(id)).findFirst().orElseThrow(GenericException::new);

//        if(addressRepository.existsById(id)){
//            addressRepository.deleteById(id);
//            return;
//        }
//        throw new IdNotFoundException();
    }

    @Override
    public CustomerProfileDTO fetchCustomerProfileDetails() {

        Customer customer = (Customer) authUserService.getCurrentAuthorizedUser();

        CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO(customer);
//            String url = "localhost:8080/images/users/";
//            customerProfileDTO.setImageUrl(url + customer.getId()+ ".jpg");
        customerProfileDTO.setImageUrl(getImageUrlIfExist(customer.getId()));

        return customerProfileDTO;

    }

    private String getImageUrlIfExist(Long id) {

        String path = "./images/users/" + id + ".jpg";

        File f = new File(path.trim());
        if (f.exists() && !f.isDirectory()) {
            // do something

            return "localhost:8080/images/users/" + id + ".jpg";
        }
        return "Not Uploaded";
    }

    @Override
    public void updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO) {

        Customer customer = (Customer) authUserService.getCurrentAuthorizedUser();

        if (customerProfileUpdateDTO.getFirstName() != null)
            customer.setFirstName(customerProfileUpdateDTO.getFirstName());

        if (customerProfileUpdateDTO.getMiddleName() != null)
            customer.setMiddleName(customerProfileUpdateDTO.getMiddleName());

        if (customerProfileUpdateDTO.getLastName() != null)
            customer.setLastName(customerProfileUpdateDTO.getLastName());

        if (customerProfileUpdateDTO.getContact() != null) customer.setContact(customerProfileUpdateDTO.getContact());

        customerRepository.save(customer);

    }


    @Override
    public List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId) {

        if (categoryId == null) {
            return categoryService.getAllRootCategories();
        }

        return categoryService.getCategoryById(categoryId).getChildren().stream().map(FetchCategoryDTO::convertCategoryToFetchCategoryDTO).toList();

    }
}
