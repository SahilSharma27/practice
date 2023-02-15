package com.sahil.ecom.security;

public interface AuthUserService {

    String getUserEmail(String accessToken);

    String getUserRole(String accessToken);


}
