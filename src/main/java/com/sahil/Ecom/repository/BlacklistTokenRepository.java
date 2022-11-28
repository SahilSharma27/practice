package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken,Long> {


    Optional<BlacklistToken>findByAccessToken(String token);

    boolean existsByAccessToken(String token);
}
