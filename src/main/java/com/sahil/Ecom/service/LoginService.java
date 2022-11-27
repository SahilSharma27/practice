package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.LoginRequestDTO;
import com.sahil.Ecom.entity.LoginResponseDTO;

public interface LoginService {

    void saveJwtResponse(LoginResponseDTO loginResponseDTO, String username);

    void removeAlreadyGeneratedTokens(LoginRequestDTO loginRequestDTO);
}
