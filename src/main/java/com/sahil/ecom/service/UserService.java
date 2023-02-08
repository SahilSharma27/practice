package com.sahil.ecom.service;


import com.sahil.ecom.dto.customer.FetchCustomerDTO;
import com.sahil.ecom.dto.seller.FetchSellerDTO;
import com.sahil.ecom.entity.Address;
import com.sahil.ecom.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean activateByEmail(String email);

    boolean activateAccount(Long id);

    boolean deActivateAccount(Long id);

    Iterable<User> getAllUsers();

    List<FetchCustomerDTO> getAllCustomersPaged(int page, int size,String sort);

    List<FetchSellerDTO> getAllSellersPaged(int page, int size,String sort);


    boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String validateActivationToken(String uuid);

    boolean forgotPasswordHelper(String email);

    String validateResetPasswordToken(String uuid);

    boolean resetPassword(String email,String newPassword);

    boolean logoutHelper(String username);

    void updateAddress(Long id,Address address,String username);

    boolean saveUserImage(Long id, MultipartFile image);

    FetchCustomerDTO getCustomer(String email);

    FetchSellerDTO getSeller(String email);

    String getRole(String username);
}
