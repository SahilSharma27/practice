package com.sahil.ecom.security;

import com.sahil.ecom.exception.GenricException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String getUserEmail(String token) {
        try {
            return jwtUtil.extractUsername(getBearerToken(token));

        } catch (Exception e) {

            e.printStackTrace();
            throw new GenricException("TOKEN INVALID");

        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer") ? token.substring("Bearer".length()) : token;
    }

    @Override
    public String getUserRole(String accessToken) {
        return null;
    }
}
