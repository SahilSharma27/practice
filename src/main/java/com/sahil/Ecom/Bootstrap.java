package com.sahil.Ecom;


import com.sahil.Ecom.entity.Address;
import com.sahil.Ecom.entity.Admin;
import com.sahil.Ecom.entity.Role;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... args) throws Exception {

//        roleRepository.deleteAll();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setAuthority("ROLE_ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setAuthority("ROLE_SELLER");

        Role role3 = new Role();
        role3.setId(3L);
        role3.setAuthority("ROLE_CUSTOMER");

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);


        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setFirstName("admin");
        admin.setMiddleName("admin");
        admin.setLastName("admin");
        admin.setPassword(bCryptPasswordEncoder.encode("admin"));
//        admin.setConfirmPassword(bCryptPasswordEncoder.encode("admin"));

        admin.setActive(true);
        admin.setExpired(false);
        admin.setDeleted(false);
        admin.setLocked(false);
        admin.setPasswordUpdateDate(new Date());

        admin.setRoles(Arrays.asList(roleRepository.findByAuthority("ROLE_ADMIN")));
        userRepository.save(admin);

    }
}

