package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String userEmail);

}
