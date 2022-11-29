package com.sahil.Ecom;

import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.repository.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
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

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

	Logger logger = LoggerFactory.getLogger(EcomApplicationTests.class);

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

	@Test
	void testMetaDataField(){

		CategoryMetaDataField colorField = new CategoryMetaDataField("COLOR");
		CategoryMetaDataField sizeField = new CategoryMetaDataField("SIZE");
		CategoryMetaDataField lengthField = new CategoryMetaDataField("LENGTH");

		categoryMetaDataFieldRepository.saveAll(Arrays.asList(colorField,sizeField,lengthField));

	}

	@Test
	void testCategory(){

		Category electronics = new Category("ELECTRONICS");

		Category mobiles = new Category("MOBILES",electronics);

		Category iphone = new Category("IPHONE",mobiles);

		Category washingMachine = new Category("WASHING MACHINE",electronics);


		electronics.addChildren(mobiles);
		electronics.addChildren(washingMachine);

		mobiles.addChildren(iphone);

		categoryRepository.save(electronics);

		Category fashion = new Category("FASHION");
		Category shirt = new Category("SHIRT",fashion);

		fashion.addChildren(shirt);

		categoryRepository.save(fashion);


	}


	@Test
	void testMetaDataFieldValue(){

//		Category fashion = new Category("Fashion");
//		Category shirt = new Category("SHIRT",fashion);
//		fashion.addChildren(shirt);
//
//		categoryRepository.save(fashion);
//
//		CategoryMetaDataField size = new CategoryMetaDataField("SIZE");
//
//		categoryMetaDataFieldRepository.save(size);

		Category category = categoryRepository.findByName("SHIRT").get();

		CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository.findByName("SIZE").get();

		CategoryMetaDataFieldValue value = new CategoryMetaDataFieldValue("S,M,L,XL,XXL");
		value.setCategoryFieldValueKey(new CategoryFieldValueKey(category.getId(),categoryMetaDataField.getId()));
		value.setCategory(category);
		value.setCategoryMetaDataField(categoryMetaDataField);

		category.addCategoryMetaDataFieldValue(value);

		categoryMetaDataField.addCategoryMetaDataFieldValue(value);

		categoryRepository.save(category);
		categoryMetaDataFieldRepository.save(categoryMetaDataField);

	}

	@Test
	void testFilePath() {

		String url = "./images/users/3.**";

		File f = new File(url.trim());

		if (f.exists() && !f.isDirectory()) {
			// do something
			logger.info("--------Exist--------------");
			logger.info(f.getAbsolutePath());
			logger.info(f.getPath());
			logger.info(f.getName());

		} else
			logger.info("--------NOPE--------------");

	}

}
