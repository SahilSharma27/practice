package com.sahil.Ecom;

import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class EcomApplicationTests {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private SellerRepository sellerRepository;

	@Test
	void contextLoads() {
	}

//    @Test
//    void testRole(){
//        roleRepository.deleteAll();
//
//        Role role1 = new Role();
//        role1.setId(1L);
//        role1.setAuthority("ADMIN");
//
//        Role role2 = new Role();
//        role2.setId(2L);
//        role2.setAuthority("SELLER");
//
//        Role role3 = new Role();
//        role3.setId(3L);
//        role3.setAuthority("CUSTOMER");
//
//        roleRepository.save(role1);
//        roleRepository.save(role2);
//        roleRepository.save(role3);
//    }
//
//	@Test
//	void testCreateCustomer(){
//
//		Customer c1 = new Customer();
//        c1.setContact("123");
//        c1.setEmail("test");
//        c1.setFirstName("test");
//        c1.setMiddleName("test");
//        c1.setLastName("test");
//        c1.setPassword("test");
//        c1.setActive(true);
//        c1.setExpired(false);
//        c1.setDeleted(false);
//        c1.setLocked(false);
//        c1.setPasswordUpdateDate(new Date());
//
//        Address a1 = new Address();
//        a1.setCity("test");
//        a1.setCountry("test");
//        a1.setAddressLine("test");
//        a1.setLabel("test");
//        a1.setZipCode("test");
//        a1.setState("test");
//       // a1.setUser(c1);
//
//        List<Address> addressList = new ArrayList<>();
//        addressList.add(a1);
//        c1.setAddresses(addressList);
//
//        c1.setRoles(Arrays.asList(roleRepository.findByAuthority("CUSTOMER")));
////
//
//        userRepository.save(c1);
//
//	}
//
//    @Test
//    void testCreateSeller(){
//
//        Seller s1 =  new Seller();
//
//        s1.setEmail("test");
//        s1.setFirstName("test");
//        s1.setMiddleName("test");
//        s1.setLastName("test");
//        s1.setPassword("test");
//        s1.setActive(true);
//        s1.setExpired(false);
//        s1.setDeleted(false);
//        s1.setLocked(false);
//        s1.setPasswordUpdateDate(new Date());
//        s1.setGst("123");
//        s1.setCompanyContact("123");
//        s1.setCompanyName("Atest");
//
//        Address a2 = new Address();
//        a2.setCity("test");
//        a2.setCountry("test");
//        a2.setAddressLine("test");
//        a2.setLabel("test");
//        a2.setZipCode("test");
//        a2.setState("test");
//
//        //a2.setUser(s1);
//
//        s1.setAddresses(Arrays.asList(a2));
//
//        s1.setRoles(Arrays.asList(roleRepository.findByAuthority("SELLER")));
//
//        userRepository.save(s1);
//
//    }

}
