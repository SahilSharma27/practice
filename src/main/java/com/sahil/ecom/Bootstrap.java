package com.sahil.ecom;


import com.sahil.ecom.entity.Admin;
import com.sahil.ecom.entity.Role;
import com.sahil.ecom.repository.RoleRepository;
import com.sahil.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private static final String ADMIN = "ADMIN";
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() < 3) {

            roleRepository.deleteAll();
            Role role1 = new Role();
            role1.setAuthority("ROLE_ADMIN");
            Role role2 = new Role();
            role2.setAuthority("ROLE_SELLER");
            Role role3 = new Role();
            role3.setAuthority("ROLE_CUSTOMER");
            roleRepository.saveAll(List.of(role1, role2, role3));


            Admin admin = new Admin();
            admin.setEmail("admin@gmail.com");
            admin.setFirstName(ADMIN);
            admin.setMiddleName(ADMIN);
            admin.setLastName(ADMIN);
            admin.setPassword(bCryptPasswordEncoder.encode(ADMIN));
            admin.setActive(true);
            admin.setExpired(false);
            admin.setDeleted(false);
            admin.setLocked(false);
            admin.setPasswordUpdateDate(new Date());

            admin.setRoles(Collections.singletonList(roleRepository.findByAuthority("ROLE_ADMIN")));
            userRepository.save(admin);

        }
    }
}

