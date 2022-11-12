package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

}
