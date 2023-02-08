package com.sahil.ecom.repository;

import com.sahil.ecom.entity.LockedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockedAccountRepository extends JpaRepository<LockedAccount,Long> {
}
