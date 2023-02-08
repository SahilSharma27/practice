package com.sahil.ecom.service.impl;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
public class UUIDTokenService {

    public String getUUIDToken(){
        return UUID.randomUUID().toString();
    }

    public String generateActivationURL(String uuid) throws MalformedURLException {

        URL domain = new URL("http://localhost:8080");
        URL url = new URL(domain + "/users/activate?token="+uuid);
        return url.toString();
    }

    public String generateResetPassURL(String uuid) throws MalformedURLException {

        URL domain = new URL("http://localhost:8080");
        URL url = new URL(domain + "/users/resetPassword?token="+uuid);
        return url.toString();
    }

}
