package com.sahil.ecom.repository;

import com.sahil.ecom.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPassTokenRepository extends JpaRepository<ResetPasswordToken,String> {
    ResetPasswordToken findByToken(String uuid);

}
