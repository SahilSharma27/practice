package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.LoginResponseDTO;

public interface LoginService {

    void saveJwtResponse(LoginResponseDTO loginResponseDTO, String username);

    void removeAlreadyGeneratedTokens(String username);
}
