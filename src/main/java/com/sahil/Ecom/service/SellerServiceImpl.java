package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    UserRepository userRepository;

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
    public Seller getSellerById(Long id){
//       if(sellerRepository.existsById(id)){
            return sellerRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("NOT FOUND"));
//        }
//        throw new UsernameNotFoundException("SELLER SERVICE:USER ID NOT FOUND");
//        return null;
    }


    @Override
    public Seller register(SellerDTO sellerDTO) {

        Seller newSeller = new Seller();

        newSeller.setEmail(sellerDTO.getEmail());
        newSeller.setFirstName(sellerDTO.getFirstName());
        newSeller.setMiddleName(sellerDTO.getMiddleName());
        newSeller.setLastName(sellerDTO.getLastName());
        newSeller.setCompanyName(sellerDTO.getCompanyName());
        newSeller.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));
        newSeller.setCompanyContact(sellerDTO.getCompanyContact());
        newSeller.setGst(sellerDTO.getGst());


        //Only one can be added
        List<Address> sellerAddressList = new ArrayList<>();
        sellerAddressList.add(sellerDTO.getAddress().mapAddressDTOtoAddress());
        newSeller.setAddresses(sellerAddressList);


        newSeller.setRoles(List.of(roleRepository.findByAuthority("ROLE_SELLER")));

        newSeller.setActive(false);
        newSeller.setDeleted(false);
        newSeller.setExpired(false);
        newSeller.setLocked(false);

        newSeller.setPasswordUpdateDate(new Date());
        newSeller.setInvalidAttemptCount(0);

        return sellerRepository.save(newSeller);
    }

    @Override
    public FetchSellerDTO fetchSellerProfileDetails(String userEmail) {
        if(userRepository.existsByEmail(userEmail)){

            Seller seller = sellerRepository.findByEmail(userEmail).orElseThrow(UserEmailNotFoundException::new);

            FetchSellerDTO fetchSellerDTO = new FetchSellerDTO();
            fetchSellerDTO.setId(seller.getId());
            fetchSellerDTO.setFullName(seller.getFirstName() + seller.getMiddleName() + seller.getLastName());
            fetchSellerDTO.setActive(seller.isActive());
            fetchSellerDTO.setEmail(userEmail);


            Address address = seller.getAddresses().get(0);

            AddressDTO addressDTO= new AddressDTO();

            addressDTO.setAddressLine(address.getAddressLine());
            addressDTO.setCity(address.getCity());
            addressDTO.setCountry(address.getCountry());
            addressDTO.setLabel(address.getLabel());
            addressDTO.setZipCode(address.getZipCode());
            addressDTO.setState(addressDTO.getState());


            fetchSellerDTO.setCompanyAddress(addressDTO);

            fetchSellerDTO.setCompanyName(seller.getCompanyName());
            fetchSellerDTO.setCompanyContact(seller.getCompanyContact());
            fetchSellerDTO.setGst(seller.getGst());

            String url = "file:///home/sahil/IdeaProjects/Ecom/images/users/";
            fetchSellerDTO.setImageUrl(url + seller.getId()+ ".jpg");

            return fetchSellerDTO;

        }

        throw new UserEmailNotFoundException("NOT FOUND");
    }


    @Override
    public boolean updateSellerProfile(String username, SellerProfileDTO sellerProfileDTO) {

        if(userRepository.existsByEmail(username)){
            Seller seller = sellerRepository.findByEmail(username).get();

            if(sellerProfileDTO.getFirstName()!=null)
                seller.setFirstName(sellerProfileDTO.getFirstName());

            if(sellerProfileDTO.getMiddleName()!=null)
                seller.setMiddleName(sellerProfileDTO.getMiddleName());

            if(sellerProfileDTO.getLastName()!=null)
                seller.setLastName(sellerProfileDTO.getLastName());

            if(sellerProfileDTO.getContact()!=null && !sellerProfileDTO.getContact().isEmpty())
                seller.setCompanyName(sellerProfileDTO.getContact());

            if(sellerProfileDTO.getCompanyName()!= null)
                seller.setCompanyName(sellerProfileDTO.getCompanyName());

            if(sellerProfileDTO.getGst()!=null){
                seller.setGst(sellerProfileDTO.getGst());
            }

            sellerRepository.save(seller);

            return true;
        }


        return false;
    }

}
