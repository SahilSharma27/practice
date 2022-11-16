package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.UserAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccessTokenRepository extends JpaRepository<UserAccessToken,String> {
    UserAccessToken findByAccessToken(String token);

}
