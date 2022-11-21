package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPassTokenRepository extends JpaRepository<ResetPasswordToken,String> {
    ResetPasswordToken findByResetPassToken(String uuid);

}
