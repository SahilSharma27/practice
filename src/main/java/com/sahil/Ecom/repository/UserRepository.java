package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
     Optional<User> findByEmail(String email);

     boolean existsByEmail(String email);

     @Modifying
     @Query("UPDATE User SET password =:newPassword WHERE email =:userEmail" )
     public int updatePassword(@Param("newPassword") String newPassword, @Param("userEmail") String userEmail);

}
