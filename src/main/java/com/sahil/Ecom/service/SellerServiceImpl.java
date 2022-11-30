package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.dto.category.FetchCategoryDTO;
import com.sahil.Ecom.dto.seller.SellerDTO;
import com.sahil.Ecom.dto.seller.SellerProfileDTO;
import com.sahil.Ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.dto.LoginRequestDTO;
import com.sahil.Ecom.dto.LoginResponseDTO;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private CategoryService categoryService;

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
            return sellerRepository.findById(id).orElseThrow(IdNotFoundException::new);
//        }
//        throw new UsernameNotFoundException("SELLER SERVICE:USER ID NOT FOUND");
//        return null;
    }


    @Override
    public boolean register(SellerDTO sellerDTO) {

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

            Seller seller = sellerRepository.findByEmail(userEmail).orElseThrow(UserEmailNotFoundException::new);

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
            sellerProfile.setCompanyAddress(new AddressDTO(seller.getAddresses().get(0)));

//            String url = "localhost:8080/images/users/"+seller.getId()+ ".jpg";
//
//            sellerProfile.setImageUrl(url + seller.getId()+ ".jpg");
            sellerProfile.setImageUrl(getImageUrlIfExist(seller.getId()));

            return sellerProfile;

    }


    @Override
    public boolean updateSellerProfile(String username, SellerProfileUpdateDTO sellerProfileUpdateDTO) {

            Seller seller = sellerRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);

            if(sellerProfileUpdateDTO.getFirstName()!=null)
                seller.setFirstName(sellerProfileUpdateDTO.getFirstName());

            if(sellerProfileUpdateDTO.getMiddleName()!=null)
                seller.setMiddleName(sellerProfileUpdateDTO.getMiddleName());

            if(sellerProfileUpdateDTO.getLastName()!=null)
                seller.setLastName(sellerProfileUpdateDTO.getLastName());

            if(sellerProfileUpdateDTO.getCompanyContact()!=null)
                seller.setCompanyContact(sellerProfileUpdateDTO.getCompanyContact());

            if(sellerProfileUpdateDTO.getCompanyName()!= null)
                seller.setCompanyName(sellerProfileUpdateDTO.getCompanyName());

            if(sellerProfileUpdateDTO.getGst()!=null){
                seller.setGst(sellerProfileUpdateDTO.getGst());
            }

            sellerRepository.save(seller);

            return true;
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
    public List<FetchCategoryDTO> getAllCategoriesForSeller() {

        List<FetchCategoryDTO> fetchCategoryList = categoryService.getAllCategories();

        return fetchCategoryList.stream().filter(fetchCategoryDTO -> fetchCategoryDTO.getChildren().size() == 0).collect(Collectors.toList());

    }
}
