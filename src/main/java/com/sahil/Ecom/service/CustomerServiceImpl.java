package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.AddressRepository;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.UserRepository;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private LockAccountService lockAccountService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean register(CustomerDTO customerDTO) {

        Customer newCustomer = new Customer();

        newCustomer.setEmail(customerDTO.getEmail());
        newCustomer.setFirstName(customerDTO.getFirstName());
        newCustomer.setMiddleName(customerDTO.getMiddleName());
        newCustomer.setLastName(customerDTO.getLastName());

        newCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        newCustomer.setContact(customerDTO.getContact());

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
    public boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO) {

        if (userRepository.existsByEmail(userEmail)) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND"));

            Address newAddress = addressDTO.mapAddressDTOtoAddress();

//            newAddress.setAddressLine(addressDTO.getAddressLine());
//            newAddress.setCity(addressDTO.getCity());
//            newAddress.setLabel(addressDTO.getLabel());
//            newAddress.setZipCode(addressDTO.getZipCode());
//            newAddress.setState(addressDTO.getState());
//            newAddress.setCountry(addressDTO.getCountry());

            user.getAddresses().add(newAddress);

            userRepository.save(user);

            return true;
        }
        return false;
    }

    @Override
    public List<Address> getAllCustomerAddresses(String userEmail) {
        if(userRepository.existsByEmail(userEmail)){

            User user =  userRepository.findByEmail(userEmail).get();
            return user.getAddresses();

        }
        throw new UserEmailNotFoundException();
    }

    @Override
    public boolean removeAddress(Long id) {
        if(addressRepository.existsById(id)){
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public FetchCustomerDTO fetchCustomerProfileDetails(String userEmail) {
        if(userRepository.existsByEmail(userEmail)){
            Customer customer = customerRepository.findByEmail(userEmail).get();

            FetchCustomerDTO fetchCustomerDTO = new FetchCustomerDTO();
            fetchCustomerDTO.setId(customer.getId());
            fetchCustomerDTO.setFullName(customer.getFirstName() + customer.getMiddleName() + customer.getLastName());
            fetchCustomerDTO.setActive(customer.isActive());
            fetchCustomerDTO.setContact(customer.getContact());
            fetchCustomerDTO.setEmail(userEmail);

            String url = "localhost:8080/images/users/";
            fetchCustomerDTO.setImageUrl(url + customer.getId()+ ".jpg");

            return fetchCustomerDTO;

        }

        throw new UserEmailNotFoundException();
    }

    @Override
    public boolean updateProfile(String username, CustomerProfileDTO customerProfileDTO) {

            Customer customer = customerRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);

            if(customerProfileDTO.getFirstName()!=null)
                customer.setFirstName(customerProfileDTO.getFirstName());

            if(customerProfileDTO.getMiddleName()!=null)
                customer.setMiddleName(customerProfileDTO.getMiddleName());

            if(customerProfileDTO.getLastName()!=null)
                customer.setLastName(customerProfileDTO.getLastName());

            if(customerProfileDTO.getContact()!=null)
                customer.setContact(customerProfileDTO.getContact());

            customerRepository.save(customer);

            return true;
    }


    @Override
    public List<FetchCategoryDTO> getAllCategoriesForCustomer(Long categoryId) {

        if(categoryId == null){
            return categoryService.getAllRootCategories();
        }

        return categoryService
                .getCategoryById(categoryId)
                .getChildren()
                .stream()
                .map(FetchCategoryDTO::new)
                .collect(Collectors.toList());

    }
}
