package com.sahil.Ecom.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_TOKEN")
public class UserAccessToken {

    @Column(name = "TOKEN")
    @Id
    private String accessToken;

    @Column(name = "EMAIL")
    private String email;

    @Column(name  = "TOKEN_TIME_LIMIT")
    private LocalDateTime tokenTimeLimit;

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

    public LocalDateTime getTokenTimeLimit() {
        return tokenTimeLimit;
    }

    public void setTokenTimeLimit(LocalDateTime tokenTimeLimit) {
        this.tokenTimeLimit = tokenTimeLimit;
    }
}
