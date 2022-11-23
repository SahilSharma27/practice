package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface AddressRepository extends JpaRepository<Address,Long> {

}
