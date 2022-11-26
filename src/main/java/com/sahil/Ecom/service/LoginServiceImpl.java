package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.repository.JwtAccessTokenRepository;
import com.sahil.Ecom.repository.JwtRefreshTokenRepository;
import com.sahil.Ecom.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService{
    @Autowired
    JwtAccessTokenRepository jwtAccessTokenRepository;

    @Autowired
    JwtRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    UserRepository userRepository;


    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    @Transactional
    public void removeAlreadyGeneratedTokens(JwtRequest jwtRequest) {

        if(userRepository.existsByEmail(jwtRequest.getUsername())){

            User user = userRepository.findByEmail(jwtRequest.getUsername()).orElse(null);

            if(jwtAccessTokenRepository.existsById(user.getId())) {

                user.setJwtAccessToken(null);
                jwtAccessTokenRepository.deleteById(user.getId());

                logger.info("--------------access tokens deleted for" + jwtRequest.getUsername());


                if (jwtRefreshTokenRepository.existsById(user.getId())) {
                    user.setJwtRefreshToken(null);
                    jwtRefreshTokenRepository.deleteById(user.getId());
                    logger.info("--------------refresh tokens deleted for" + jwtRequest.getUsername());
                }
            }


            logger.info("--------------LOGGED IN FOR FIRST TIME" + jwtRequest.getUsername());
        }

    }

    @Override
    public void saveJwtResponse(JwtResponse jwtResponse, String username) {

        User user = userRepository.findByEmail(username).get();

        //save refresh token
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
        jwtRefreshToken.setRefreshToken(jwtResponse.getRefreshToken());
        jwtRefreshToken.setUser(user);


        //save access token
        JwtAccessToken jwtAccessToken = new JwtAccessToken();
        jwtAccessToken.setAccessToken(jwtResponse.getAccessToken());
        jwtAccessToken.setUser(user);

//        jwtRefreshToken.setJwtAccessToken(List.of(jwtAccessToken));


        jwtRefreshTokenRepository.save(jwtRefreshToken);

        jwtAccessTokenRepository.save(jwtAccessToken);

    }
}
