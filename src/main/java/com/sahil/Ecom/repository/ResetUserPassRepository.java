package com.sahil.Ecom.repository;

import com.sahil.Ecom.dto.ForgotPasswordDTO;
import com.sahil.Ecom.entity.ResetPassToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetUserPassRepository extends JpaRepository<ResetPassToken,String> {

    ResetPassToken findByResetPassToken(String uuid);
}
