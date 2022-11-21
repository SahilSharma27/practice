package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.JwtAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtAccessTokenRepository extends JpaRepository<JwtAccessToken,Long> {
}
