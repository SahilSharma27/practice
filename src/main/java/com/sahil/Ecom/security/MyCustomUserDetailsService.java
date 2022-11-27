package com.sahil.Ecom.security;

import com.sahil.Ecom.entity.Role;
import com.sahil.Ecom.exception.AccountNotActiveException;
import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MyCustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(MyCustomUserDetailsService.class);

    @Autowired
    MessageSource messageSource;

    Locale locale = LocaleContextHolder.getLocale();

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("-------------------------------" + email);

            com.sahil.Ecom.entity.User user  =  userRepository.findByEmail(email).orElseThrow(
                    ()-> new UsernameNotFoundException(messageSource.getMessage("username.not.found",null,"message",locale)));

//        if(user.isActive()) {
            boolean enabled = !user.isActive();
            boolean locked = user.isLocked();

            return User.withUsername(user.getEmail())
                    .password(user.getPassword())
                    .disabled(enabled)
                    .accountLocked(locked)
                    .authorities(mapRolesToAuthority(user.getRoles())).build();
//        }


//        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRoles()));

    }

    private Collection<GrantedAuthority> mapRolesToAuthority(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }


}
