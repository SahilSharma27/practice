package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.JwtAccessToken;
import com.sahil.Ecom.entity.JwtRefreshToken;
import com.sahil.Ecom.entity.JwtResponse;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.repository.JwtAccessTokenRepository;
import com.sahil.Ecom.repository.JwtRefreshTokenRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    JwtAccessTokenRepository jwtAccessTokenRepository;

    @Autowired
    JwtRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveJwtResponse(JwtResponse jwtResponse, String username) {

        User user = userRepository.findByEmail(username).get();

        //save access token
        JwtAccessToken jwtAccessToken = new JwtAccessToken();
        jwtAccessToken.setAccessToken(jwtResponse.getAccessToken());
        jwtAccessToken.setUser(user);
        jwtAccessTokenRepository.save(jwtAccessToken);


        //save refresh token
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
        jwtRefreshToken.setRefreshToken(jwtResponse.getRefreshToken());
        jwtRefreshToken.setUser(user);
        jwtRefreshTokenRepository.save(jwtRefreshToken);

    }
}
