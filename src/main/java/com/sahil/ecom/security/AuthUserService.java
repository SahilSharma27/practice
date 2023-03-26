package com.sahil.ecom.security;

import com.sahil.ecom.entity.User;

public interface AuthUserService {

    User getCurrentAuthorizedUser();

}
