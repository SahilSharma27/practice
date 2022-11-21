package com.sahil.Ecom.service;

import com.sahil.Ecom.entity.JwtResponse;

public interface LoginService {

    void saveJwtResponse(JwtResponse jwtResponse, String username);
}
