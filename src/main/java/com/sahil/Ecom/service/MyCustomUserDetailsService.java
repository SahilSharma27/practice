package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.Role;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyCustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.sahil.Ecom.entity.User user =  userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("User Email not found"));

        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRoles()));

//        if(email.equals("sahil@gmail.com")){
//            return new User("sahil@gmail.com","123", Arrays.asList());
//        }
//        else{
//            throw new UsernameNotFoundException("User not found");
//        }

//        AppUser appUser = appUsersRepository.findByUsername(username);
//
//        if(appUser.getUsername().equals(username)){
//            return new User(appUser);
//        }
//        else{
//            throw new UsernameNotFoundException("user not found");
//        }
    }

    private Collection<GrantedAuthority> mapRolesToAuthority(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }
}
