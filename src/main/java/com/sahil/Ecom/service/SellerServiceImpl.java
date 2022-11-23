package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.dto.SellerDTO;
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
        if(sellerRepository.existsById(id)){
            return sellerRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("NOT FOUND"));
        }
        throw new UsernameNotFoundException("SELLER SERVICE:USER ID NOT FOUND");
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

    @Override
    public FetchSellerDTO fetchSellerProfileDetails(String userEmail) {
        if(userRepository.existsByEmail(userEmail)){

            Seller seller = sellerRepository.findByEmail(userEmail).get();

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

}
