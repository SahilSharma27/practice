package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.ActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken,String> {
    ActivationToken findByActivationToken(String token);
}
