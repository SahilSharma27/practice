package com.sahil.Ecom;

import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import com.sahil.Ecom.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
class EcomApplicationTests {

    @Autowired
    private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testCreate(){

		Customer c1 =  new Customer();
        c1.setContact("123");
        c1.setEmail("test");
        c1.setFirstName("test");
        c1.setMiddleName("test");
        c1.setLastName("test");
        c1.setPassword("test");
        c1.setActive(true);
        c1.setExpired(false);
        c1.setDeleted(false);
        c1.setLocked(false);
        c1.setPasswordUpdateDate(new Date());

        Address a1 = new Address();
        a1.setCity("test");
        a1.setCountry("test");
        a1.setAddressLine("test");
        a1.setLabel("test");
        a1.setZipCode("test");
        a1.setState("test");
        a1.setUser(c1);

        Set<Address> addressSet = new HashSet<>();
        addressSet.add(a1);

        c1.setAddresses(addressSet);

        userRepository.save(c1);

	}

    @Test
    void testCreate1(){

        Seller s1 =  new Seller();

        s1.setEmail("test");
        s1.setFirstName("test");
        s1.setMiddleName("test");
        s1.setLastName("test");
        s1.setPassword("test");
        s1.setActive(true);
        s1.setExpired(false);
        s1.setDeleted(false);
        s1.setLocked(false);
        s1.setPasswordUpdateDate(new Date());
        s1.setGst("123");
        s1.setCompanyContact("123");
        s1.setCompanyName("Atest");

        userRepository.save(s1);


    }

}
