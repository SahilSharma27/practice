package com.sahil.ecom.service;

import com.sahil.ecom.dto.LoginResponseDTO;

public interface LoginService {

    void saveJwtResponse(LoginResponseDTO loginResponseDTO, String username);

    void removeAlreadyGeneratedTokens(String username);
}
