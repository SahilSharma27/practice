package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken,String> {

    ActivationToken findByActivationToken(String token);

    @Modifying
    void deleteByUserEmail(String userEmail);
}
