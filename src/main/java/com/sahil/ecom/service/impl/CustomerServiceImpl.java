package com.sahil.ecom.service.impl;


import com.sahil.ecom.dto.address.AddAddressDTO;
import com.sahil.ecom.dto.address.FetchAddressDTO;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.ecom.entity.Address;
import com.sahil.ecom.entity.Customer;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.enums.EcomRoles;
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
import com.sahil.ecom.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private final UserService userService;

    @Override
    public boolean register(AddCustomerDTO addCustomerDTO) {
        validateCustomerDetails(addCustomerDTO);
        saveNewCustomer(addCustomerDTO);
        userService.activationHelper(addCustomerDTO.getEmail());
        return true;
    }

    private void saveNewCustomer(AddCustomerDTO addCustomerDTO) {
        Customer newCustomer = new Customer();

        newCustomer.setEmail(addCustomerDTO.getEmail());
        newCustomer.setFirstName(addCustomerDTO.getFirstName());
        newCustomer.setMiddleName(addCustomerDTO.getMiddleName());
        newCustomer.setLastName(addCustomerDTO.getLastName());

        newCustomer.setPassword(passwordEncoder.encode(addCustomerDTO.getPassword()));
        newCustomer.setContact(addCustomerDTO.getContact());

        newCustomer.setRoles(Collections.singletonList(roleRepository.findByAuthority(EcomRoles.CUSTOMER.role)));

        newCustomer.setActive(false);
        newCustomer.setDeleted(false);
        newCustomer.setExpired(false);
        newCustomer.setLocked(false);
        newCustomer.setPasswordUpdateDate(new Date());
        newCustomer.setInvalidAttemptCount(0);


        customerRepository.save(newCustomer);
    }

    private void validateCustomerDetails(AddCustomerDTO addCustomerDTO) {
        //check pass and cpass
        if (!addCustomerDTO.getPassword().equals(addCustomerDTO.getConfirmPassword()))
            throw new GenericException("Password confirm password not matching");

        //Check if email taken
        if (userService.checkUserEmail(addCustomerDTO.getEmail())) {
            throw new GenericException("Email already registered");
        }

    }

    @Override
    public LoginResponseDTO loginCustomer(LoginRequestDTO loginRequestDTO) {

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return loginResponseDTO;


    }


    @Override
    public boolean addAddressToCustomer(AddAddressDTO addAddressDTO) {

        User user = authUserService.getCurrentAuthorizedUser();
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
    public boolean removeAddress(Long id) {
        User user = authUserService.getCurrentAuthorizedUser();
//        user.getAddresses().forEach(address ->{
//            if(address.getId().equals(id)){
//                addressRepository.deleteById(id);
//                return;
//            }
//        });

        user.getAddresses().stream().filter(address -> address.getId().equals(id)).findFirst().orElseThrow(GenericException::new);
        return true;
//        if(addressRepository.existsById(id)){
//            addressRepository.deleteById(id);
//            return;
//        }
//        throw new IdNotFoundException();
    }

    @Override
    public CustomerProfileDTO fetchCustomerProfileDetails() {

        Customer customer = (Customer) authUserService.getCurrentAuthorizedUser();

        return CustomerProfileDTO.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .isActive(customer.isActive())
                .contact(customer.getContact())
                .imageUrl(getImageUrlIfExist(customer.getId()))
                .build();
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
    public boolean updateProfile(CustomerProfileUpdateDTO customerProfileUpdateDTO) {

        Customer customer = (Customer) authUserService.getCurrentAuthorizedUser();

        if (customerProfileUpdateDTO.getFirstName() != null)
            customer.setFirstName(customerProfileUpdateDTO.getFirstName());

        if (customerProfileUpdateDTO.getMiddleName() != null)
            customer.setMiddleName(customerProfileUpdateDTO.getMiddleName());

        if (customerProfileUpdateDTO.getLastName() != null)
            customer.setLastName(customerProfileUpdateDTO.getLastName());

        if (customerProfileUpdateDTO.getContact() != null) customer.setContact(customerProfileUpdateDTO.getContact());

        customerRepository.save(customer);
        return true;

    }


    @Override
    public List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId) {

        if (Objects.isNull(categoryId)) {
            return categoryService.getAllRootCategories();
        }
        return categoryService.getCategoryById(categoryId)
                .getChildren()
                .stream()
                .map(FetchCategoryDTO::convertCategoryToFetchCategoryDTO)
                .toList();

    }
}
