package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

}
