package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

    public Role findByAuthority(String authority);

}
