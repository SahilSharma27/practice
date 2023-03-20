package com.sahil.ecom.repository;

import com.sahil.ecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User SET isActive =:isActive WHERE email =:email")
    int updateIsActive(@Param("isActive") boolean isActive, @Param("email") String email);

    @Modifying
    @Query("UPDATE User SET invalidAttemptCount =:i WHERE email =:email")
    int updateInvalidAttempts(@Param("i") int i, @Param("email") String email);

    @Modifying
    @Query("UPDATE User SET isLocked =:isLocked WHERE email =:email")
    int updateIsLocked(@Param("isLocked") boolean isLocked, @Param("email") String email);


    @Modifying
    @Query("UPDATE User SET password =:newPassword,passwordUpdateDate = CURRENT_TIMESTAMP WHERE email =:userEmail")
    int updatePassword(@Param("newPassword") String newPassword, @Param("userEmail") String userEmail);


}
