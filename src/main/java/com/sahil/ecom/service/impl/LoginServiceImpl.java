package com.sahil.ecom.service.impl;

import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.entity.*;
import com.sahil.ecom.exception.UserEmailNotFoundException;
import com.sahil.ecom.repository.JwtAccessTokenRepository;
import com.sahil.ecom.repository.JwtRefreshTokenRepository;
import com.sahil.ecom.repository.UserRepository;
import com.sahil.ecom.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    JwtAccessTokenRepository jwtAccessTokenRepository;

    @Autowired
    JwtRefreshTokenRepository jwtRefreshTokenRepository;

    @Autowired
    UserRepository userRepository;


    Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Override
    @Transactional
    public void removeAlreadyGeneratedTokens(String username) {

        if(userRepository.existsByEmail(username)){

            User user = userRepository.findByEmail(username).orElseThrow(UserEmailNotFoundException::new);

            if(jwtAccessTokenRepository.existsById(user.getId())) {

                user.setJwtAccessToken(null);
                jwtAccessTokenRepository.deleteById(user.getId());

                logger.info("--------------access tokens deleted for" + username);


                if (jwtRefreshTokenRepository.existsById(user.getId())) {
                    user.setJwtRefreshToken(null);
                    jwtRefreshTokenRepository.deleteById(user.getId());
                    logger.info("--------------refresh tokens deleted for" + username);
                }
            }


         //   logger.info("--------------LOGGED IN FOR FIRST TIME" + loginRequestDTO.getUsername());
        }

    }

    @Override
    public void saveJwtResponse(LoginResponseDTO loginResponseDTO, String username) {

        User user = userRepository.findByEmail(username).get();

        //save refresh token
        JwtRefreshToken jwtRefreshToken = new JwtRefreshToken();
        jwtRefreshToken.setRefreshToken(loginResponseDTO.getRefreshToken());
        jwtRefreshToken.setUser(user);


        //save access token
        JwtAccessToken jwtAccessToken = new JwtAccessToken();
        jwtAccessToken.setAccessToken(loginResponseDTO.getAccessToken());
        jwtAccessToken.setUser(user);

//        jwtRefreshToken.setJwtAccessToken(List.of(jwtAccessToken));


        jwtRefreshTokenRepository.save(jwtRefreshToken);

        jwtAccessTokenRepository.save(jwtAccessToken);

    }
}
