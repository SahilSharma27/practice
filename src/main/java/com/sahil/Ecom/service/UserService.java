package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.customer.FetchCustomerDTO;
import com.sahil.Ecom.dto.seller.FetchSellerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean activateByEmail(String email);

    boolean activateAccount(Long id);

    boolean deActivateAccount(Long id);

    Iterable<User> getAllUsers();

//    List<FetchCustomerDTO> getAllCustomers();

    List<FetchCustomerDTO> getAllCustomersPaged(int page, int size,String sort);

    List<FetchSellerDTO> getAllSellersPaged(int page, int size,String sort);

//    List<FetchSellerDTO> getAllSellers();

    boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String validateActivationToken(String uuid);

    boolean forgotPasswordHelper(String email);

    String validateResetPasswordToken(String uuid);

    boolean resetPassword(String email,String newPassword);

    void sendSellerAcknowledgement(String email);

    boolean logoutHelper(String username);

    void updateAddress(Long id,Address address,String username);

    boolean saveUserImage(Long id, MultipartFile image);

    FetchCustomerDTO getCustomer(String email);

    FetchSellerDTO getSeller(String email);
}
