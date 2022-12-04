package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.dto.category.FetchCategoryDTO;
import com.sahil.Ecom.dto.customer.AddCustomerDTO;
import com.sahil.Ecom.dto.customer.CustomerProfileDTO;
import com.sahil.Ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.exception.UniqueFieldException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.AddressRepository;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.UserRepository;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
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

    @Autowired
    private MessageSource messageSource;

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
    public boolean addAddressToCustomer(String userEmail, AddAddressDTO addAddressDTO) {

            User user = userRepository.findByEmail(userEmail).orElseThrow(UserEmailNotFoundException::new);

            //check address already exist
            user.getAddresses().forEach(address -> {
                if(address.getLabel().equalsIgnoreCase(addAddressDTO.getLabel())){
                    throw new UniqueFieldException(
                            messageSource.getMessage("same.address.exist",null,"message", LocaleContextHolder.getLocale()
                            )
                    );
                }
            });

            Address newAddress = addAddressDTO.mapAddressDTOtoAddress();
            user.getAddresses().add(newAddress);

            userRepository.save(user);
            return true;

    }

    @Override
    public List<FetchAddressDTO> getAllCustomerAddresses(String userEmail) {

        User user =  userRepository
                .findByEmail(userEmail)
                .orElseThrow(UserEmailNotFoundException::new);

        return user.getAddresses()
                .stream().map(FetchAddressDTO::new)
                .collect(Collectors.toList());

    }

    @Override
    public void removeAddress(Long id,String userEmail) {

        User user =  userRepository.findByEmail(userEmail).orElseThrow(UserEmailNotFoundException::new);

//        user.getAddresses().forEach(address ->{
//            if(address.getId().equals(id)){
//                addressRepository.deleteById(id);
//                return;
//            }
//        });

        user.getAddresses()
                .stream()
                .filter(address -> address.getId().equals(id))
                .findFirst().orElseThrow(IdNotFoundException::new);

//        if(addressRepository.existsById(id)){
//            addressRepository.deleteById(id);
//            return;
//        }
//        throw new IdNotFoundException();
    }

    @Override
    public CustomerProfileDTO fetchCustomerProfileDetails(String userEmail) {

            Customer customer = customerRepository.findByEmail(userEmail).orElseThrow(UserEmailNotFoundException::new);

            CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO(customer);
//            String url = "localhost:8080/images/users/";
//            customerProfileDTO.setImageUrl(url + customer.getId()+ ".jpg");
        customerProfileDTO.setImageUrl(getImageUrlIfExist(customer.getId()));

            return customerProfileDTO;

    }

    private String getImageUrlIfExist(Long id){

        String path = "./images/users/"+id+ ".jpg";

        File f = new File(path.trim());
        if(f.exists() && !f.isDirectory()) {
            // do something

            return "localhost:8080/images/users/"+id+".jpg";
        }
        return "Not Uploaded";
    }

    @Override
    public void updateProfile(String username, CustomerProfileUpdateDTO customerProfileUpdateDTO) {

            Customer customer = customerRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);

            if(customerProfileUpdateDTO.getFirstName()!=null)
                customer.setFirstName(customerProfileUpdateDTO.getFirstName());

            if(customerProfileUpdateDTO.getMiddleName()!=null)
                customer.setMiddleName(customerProfileUpdateDTO.getMiddleName());

            if(customerProfileUpdateDTO.getLastName()!=null)
                customer.setLastName(customerProfileUpdateDTO.getLastName());

            if(customerProfileUpdateDTO.getContact()!=null)
                customer.setContact(customerProfileUpdateDTO.getContact());

            customerRepository.save(customer);

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
