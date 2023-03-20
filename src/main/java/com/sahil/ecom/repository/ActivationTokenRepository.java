package com.sahil.ecom.repository;

import com.sahil.ecom.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken,String> {

    ActivationToken findByToken(String token);

    @Modifying
    void deleteByUserEmail(String userEmail);
}
