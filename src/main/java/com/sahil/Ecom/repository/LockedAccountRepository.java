package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.LockedAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockedAccountRepository extends JpaRepository<LockedAccount,Long> {
}
