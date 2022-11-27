package com.sahil.Ecom.service;


import com.sahil.Ecom.dto.AddressDTO;
import com.sahil.Ecom.dto.FetchCustomerDTO;
import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface UserService {

//    Customer login(Customer customer);
//
//    Seller login(Seller seller);

    User activateByEmail(String email);

    boolean activateAccount(Long id);

    boolean deActivateAccount(Long id);

    Iterable<User> getAllUsers();

    List<FetchCustomerDTO> getAllCustomers();

    List<FetchCustomerDTO> getAllCustomersPaged(int page, int size,String sort);

    List<FetchSellerDTO> getAllSellersPaged(int page, int size,String sort);

    List<FetchSellerDTO> getAllSellers();

    boolean checkUserEmail(String email);

    void  activationHelper(String email);

    String validateActivationToken(String uuid);

    void forgotPasswordHelper(String email);

    String validateResetPasswordToken(String uuid);


    boolean resetPassword(String email,String newPassword);

    void sendSellerAcknowledgement(String email);

    boolean logout(String accessToken);

    boolean updateAddress(Long id,Address address);

    boolean saveUserImage(Long id, MultipartFile image);
}
