package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken,String> {
}
