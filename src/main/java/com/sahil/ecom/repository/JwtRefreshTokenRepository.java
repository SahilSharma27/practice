package com.sahil.ecom.repository;

import com.sahil.ecom.entity.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken,Long> {
}
