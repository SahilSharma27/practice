package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    SellerRepository sellerRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public boolean checkSellerCompanyName(String companyName) {
        return sellerRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean checkSellerGst(String gst) {
        return sellerRepository.existsByGst(gst);
    }


    @Override
    public Seller register(SellerDTO sellerDTO) {

        Seller newSeller = new Seller();

        newSeller.setEmail(sellerDTO.getEmail());
        newSeller.setFirstName(sellerDTO.getFirstName());
        newSeller.setMiddleName(sellerDTO.getMiddleName());
        newSeller.setLastName(sellerDTO.getLastName());
        newSeller.setCompanyName(sellerDTO.getCompanyName());

        //Only one can be added
        List<Address> sellerAddressList = new ArrayList<>();

        Address sellerAddress = new Address();
        sellerAddress.setAddressLine(sellerDTO.getAddress().getAddressLine());
        sellerAddress.setCity(sellerDTO.getAddress().getCity());
        sellerAddress.setCountry(sellerDTO.getAddress().getCountry());
        sellerAddress.setLabel(sellerDTO.getAddress().getLabel());
        sellerAddress.setZipCode(sellerDTO.getAddress().getZipCode());
        sellerAddress.setState(sellerDTO.getAddress().getState());

        sellerAddressList.add(sellerAddress);

        newSeller.setAddresses(sellerAddressList);

        newSeller.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));
        newSeller.setCompanyContact(sellerDTO.getCompanyContact());
        newSeller.setGst(sellerDTO.getGst());

        newSeller.setRoles(Arrays.asList(roleRepository.findByAuthority("ROLE_SELLER")));

        newSeller.setActive(false);
        newSeller.setDeleted(false);
        newSeller.setExpired(false);
        newSeller.setLocked(false);

        newSeller.setPasswordUpdateDate(new Date());
        newSeller.setInvalidAttemptCount(0);

        return sellerRepository.save(newSeller);
    }

}
