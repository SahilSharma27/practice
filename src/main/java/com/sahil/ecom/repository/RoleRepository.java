package com.sahil.ecom.repository;

import com.sahil.ecom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);

}
