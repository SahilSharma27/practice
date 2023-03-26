package com.sahil.ecom.service.impl;

import com.sahil.ecom.dto.FetchAddressDTO;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.seller.AddSellerDTO;
import com.sahil.ecom.dto.seller.SellerProfileDTO;
import com.sahil.ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.ecom.entity.Address;
import com.sahil.ecom.entity.Seller;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.RoleRepository;
import com.sahil.ecom.repository.SellerRepository;
import com.sahil.ecom.repository.UserRepository;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.CategoryService;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.SellerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LoginService loginService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final CategoryService categoryService;

    @Override
    public boolean checkSellerCompanyName(String companyName) {
        return sellerRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean checkSellerGst(String gst) {
        return sellerRepository.existsByGst(gst);
    }

    @Override
    public Seller getSellerById(Long id) {
//       if(sellerRepository.existsById(id)){
        return sellerRepository.findById(id).orElseThrow(GenericException::new);
//        }
//        throw new UsernameNotFoundException("SELLER SERVICE:USER ID NOT FOUND");
//        return null;
    }


    @Override
    public boolean register(AddSellerDTO addSellerDTO) {

        Seller newSeller = new Seller();

        newSeller.setEmail(addSellerDTO.getEmail());
        newSeller.setFirstName(addSellerDTO.getFirstName());
        newSeller.setMiddleName(addSellerDTO.getMiddleName());
        newSeller.setLastName(addSellerDTO.getLastName());
        newSeller.setCompanyName(addSellerDTO.getCompanyName());
        newSeller.setPassword(passwordEncoder.encode(addSellerDTO.getPassword()));
        newSeller.setCompanyContact(addSellerDTO.getCompanyContact());
        newSeller.setGst(addSellerDTO.getGst());


        //Only one can be added
        List<Address> sellerAddressList = new ArrayList<>();
        Address addressToBeSaved = addSellerDTO.getAddress().mapAddressDTOtoAddress();
        addressToBeSaved.setCreatedBy(newSeller.getEmail());
//        addressToBeSaved.set
        sellerAddressList.add(addressToBeSaved);

        newSeller.setAddresses(sellerAddressList);


        newSeller.setRoles(List.of(roleRepository.findByAuthority("ROLE_SELLER")));

        newSeller.setActive(false);
        newSeller.setDeleted(false);
        newSeller.setExpired(false);
        newSeller.setLocked(false);

        newSeller.setPasswordUpdateDate(new Date());
        newSeller.setInvalidAttemptCount(0);

        sellerRepository.save(newSeller);

        return true;

    }


    @Override
    public LoginResponseDTO loginSeller(LoginRequestDTO loginRequestDTO) throws Exception {

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return loginResponseDTO;

    }


    @Override
    public SellerProfileDTO fetchSellerProfileDetails(String userEmail) {

        Seller seller = sellerRepository.findByEmail(userEmail).orElseThrow(GenericException::new);

        SellerProfileDTO sellerProfile = new SellerProfileDTO();

        sellerProfile.setId(seller.getId());
        sellerProfile.setFirstName(seller.getFirstName());
        sellerProfile.setMiddleName(seller.getMiddleName());
        sellerProfile.setLastName(seller.getLastName());
        sellerProfile.setActive(seller.isActive());
        sellerProfile.setEmail(userEmail);
        sellerProfile.setCompanyName(seller.getCompanyName());
        sellerProfile.setCompanyContact(seller.getCompanyContact());
        sellerProfile.setGst(seller.getGst());
        sellerProfile.setCompanyAddress(new FetchAddressDTO(seller.getAddresses().get(0)));

        sellerProfile.setImageUrl(getImageUrlIfExist(seller.getId()));

        return sellerProfile;

    }


    @Override
    public boolean updateSellerProfile(String username, SellerProfileUpdateDTO sellerProfileUpdateDTO) {

        Seller seller = sellerRepository.findByEmail(username).orElseThrow(GenericException::new);

        if (sellerProfileUpdateDTO.getFirstName() != null)
            seller.setFirstName(sellerProfileUpdateDTO.getFirstName());

        if (sellerProfileUpdateDTO.getMiddleName() != null)
            seller.setMiddleName(sellerProfileUpdateDTO.getMiddleName());

        if (sellerProfileUpdateDTO.getLastName() != null)
            seller.setLastName(sellerProfileUpdateDTO.getLastName());

        if (sellerProfileUpdateDTO.getCompanyContact() != null)
            seller.setCompanyContact(sellerProfileUpdateDTO.getCompanyContact());

        if (sellerProfileUpdateDTO.getCompanyName() != null)
            seller.setCompanyName(sellerProfileUpdateDTO.getCompanyName());

        if (sellerProfileUpdateDTO.getGst() != null) {
            seller.setGst(sellerProfileUpdateDTO.getGst());
        }

        sellerRepository.save(seller);

        return true;
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
    public List<FetchCategoryDTO> getAllCategoriesForSeller() {

        List<FetchCategoryDTO> fetchCategoryList = categoryService.getAllCategories();

        return fetchCategoryList.stream().filter(fetchCategoryDTO -> fetchCategoryDTO.getChildren().isEmpty()).toList();

    }
}
