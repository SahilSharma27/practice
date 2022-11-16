package com.sahil.Ecom.service;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
public class AccessTokenService {

    public String getAccessToken(){
        return UUID.randomUUID().toString();
    }

    public String generateURL(String uuid) throws MalformedURLException {

        URL domain = new URL("http://localhost:8080");
        URL url = new URL(domain + "/users/activate?token="+uuid);
        return url.toString();
    }

}
