package com.sahil.ecom.repository;

import com.sahil.ecom.entity.JwtAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtAccessTokenRepository extends JpaRepository<JwtAccessToken,Long> {
}
