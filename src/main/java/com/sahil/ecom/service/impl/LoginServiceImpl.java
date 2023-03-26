package com.sahil.ecom.service.impl;

import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.entity.JwtAccessToken;
import com.sahil.ecom.entity.JwtRefreshToken;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.JwtAccessTokenRepository;
import com.sahil.ecom.repository.JwtRefreshTokenRepository;
import com.sahil.ecom.repository.UserRepository;
import com.sahil.ecom.service.LoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private final JwtAccessTokenRepository jwtAccessTokenRepository;
    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void removeAlreadyGeneratedTokens(String username) {

        if (userRepository.existsByEmail(username)) {

            User user = userRepository.findByEmail(username).orElseThrow(GenericException::new);

            if (jwtAccessTokenRepository.existsById(user.getId())) {

                user.setJwtAccessToken(null);
                jwtAccessTokenRepository.deleteById(user.getId());

                log.info("--------------access tokens deleted for" + username);


                if (jwtRefreshTokenRepository.existsById(user.getId())) {
                    user.setJwtRefreshToken(null);
                    jwtRefreshTokenRepository.deleteById(user.getId());
                    log.info("--------------refresh tokens deleted for" + username);
                }
            }


            //   log.info("--------------LOGGED IN FOR FIRST TIME" + loginRequestDTO.getUsername());
        }

    }

    @Override
    public void saveJwtResponse(LoginResponseDTO loginResponseDTO, String username) {

        User user = userRepository.findByEmail(username).orElseThrow(GenericException::new);

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
