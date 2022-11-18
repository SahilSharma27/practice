package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.CustomerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.repository.CustomerRepository;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Customer register(CustomerDTO customerDTO) {

        Customer newCustomer = new Customer();

        newCustomer.setEmail(customerDTO.getEmail());
        newCustomer.setFirstName(customerDTO.getFirstName());
        newCustomer.setMiddleName(customerDTO.getMiddleName());
        newCustomer.setLastName(customerDTO.getLastName());

        List<Address > addressList = mapAddressDTOtoAddressEntity(customerDTO.getAddressList());

        newCustomer.setAddresses(addressList);
        newCustomer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        newCustomer.setContact(customerDTO.getContact());

        newCustomer.setRoles(Arrays.asList(roleRepository.findByAuthority("ROLE_CUSTOMER")));

        newCustomer.setActive(false);
        newCustomer.setDeleted(false);
        newCustomer.setExpired(false);
        newCustomer.setLocked(false);
        newCustomer.setPasswordUpdateDate(new Date());
        newCustomer.setInvalidAttemptCount(0);

        return customerRepository.save(newCustomer);

    }

    private List<Address> mapAddressDTOtoAddressEntity(List<AddressDTO> addressDTOList) {

        List<Address> addresses = new ArrayList<>();

        for (AddressDTO addressDTO:addressDTOList) {

            //map values
            Address customerAddress = new Address();
            customerAddress.setAddressLine(addressDTO.getAddressLine());
            customerAddress.setCity(addressDTO.getCity());
            customerAddress.setCountry(addressDTO.getCountry());
            customerAddress.setLabel(addressDTO.getLabel());
            customerAddress.setZipCode(addressDTO.getZipCode());
            customerAddress.setState(addressDTO.getState());

            //add to list
            addresses.add(customerAddress);

        }

        return addresses;
    }
}
