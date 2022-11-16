package com.sahil.Ecom.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER_TOKEN")
public class UserAccessToken {

    @Column(name = "TOKEN")
    @Id
    private String accessToken;

    @Column(name = "EMAIL")
    private String email;

    public UserAccessToken() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
