package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.JwtRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken,Long> {
}
