package com.sahil.Ecom;


import com.sahil.Ecom.entity.Role;
import com.sahil.Ecom.repository.RoleRepository;
import com.sahil.Ecom.repository.SellerRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public void run(String... args) throws Exception {

//        roleRepository.deleteAll();

        Role role1 = new Role();
        role1.setId(1L);
        role1.setAuthority("ADMIN");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setAuthority("SELLER");

        Role role3 = new Role();
        role3.setId(3L);
        role3.setAuthority("CUSTOMER");

        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);

    }
}

