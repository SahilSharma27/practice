package com.sahil.ecom.security;

import com.sahil.ecom.entity.User;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentAuthorizedUser() {
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByEmail(username).orElseThrow(GenericException::new);

    }
}
