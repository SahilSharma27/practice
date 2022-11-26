package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.AddressRepository;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public Customer register(CustomerDTO customerDTO) {

        Customer newCustomer = new Customer();

        newCustomer.setEmail(customerDTO.getEmail());
        newCustomer.setFirstName(customerDTO.getFirstName());
        newCustomer.setMiddleName(customerDTO.getMiddleName());
        newCustomer.setLastName(customerDTO.getLastName());

//        List<Address > addressList = mapAddressDTOtoAddressEntity(customerDTO.getAddressList());

//        newCustomer.setAddresses(addressList);
        newCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        newCustomer.setContact(customerDTO.getContact());

        newCustomer.setRoles(Collections.singletonList(roleRepository.findByAuthority("ROLE_CUSTOMER")));

        newCustomer.setActive(false);
        newCustomer.setDeleted(false);
        newCustomer.setExpired(false);
        newCustomer.setLocked(false);
        newCustomer.setPasswordUpdateDate(new Date());
        newCustomer.setInvalidAttemptCount(0);

        return customerRepository.save(newCustomer);

    }

//    private List<Address> mapAddressDTOtoAddressEntity(List<AddressDTO> addressDTOList) {
//
//        List<Address> addresses = new ArrayList<>();
//
//        for (AddressDTO addressDTO:addressDTOList) {
//
//            //map values
//            Address customerAddress = new Address();
//            customerAddress.setAddressLine(addressDTO.getAddressLine());
//            customerAddress.setCity(addressDTO.getCity());
//            customerAddress.setCountry(addressDTO.getCountry());
//            customerAddress.setLabel(addressDTO.getLabel());
//            customerAddress.setZipCode(addressDTO.getZipCode());
//            customerAddress.setState(addressDTO.getState());
//
//            //add to list
//            addresses.add(customerAddress);
//
//        }
//
//        return addresses;
//    }

    @Override
    public boolean addAddressToCustomer(String userEmail, AddressDTO addressDTO) {

        if (userRepository.existsByEmail(userEmail)) {
            User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND"));

            Address newAddress = new Address();

            newAddress.setAddressLine(addressDTO.getAddressLine());
            newAddress.setCity(addressDTO.getCity());
            newAddress.setLabel(addressDTO.getLabel());
            newAddress.setZipCode(addressDTO.getZipCode());
            newAddress.setState(addressDTO.getState());
            newAddress.setCountry(addressDTO.getCountry());

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
        throw new UsernameNotFoundException("NOT FOUND");
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

        throw new UserEmailNotFoundException("NOT FOUND");
    }

    @Override
    public boolean updateProfile(String username, CustomerProfileDTO customerProfileDTO) {

        if(userRepository.existsByEmail(username)){
            Customer customer = customerRepository.findByEmail(username).get();

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


        return false;
    }
}
