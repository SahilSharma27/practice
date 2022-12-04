package com.sahil.Ecom.entity;

import com.sahil.Ecom.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ACTIVATION_TOKEN")
public class ActivationToken extends Auditable {

    @Column(name = "TOKEN")
    @Id
    private String activationToken;

    @Column(name = "USER_EMAIL")
    private String userEmail;

    @Column(name  = "TOKEN_TIME_LIMIT")
    private LocalDateTime tokenTimeLimit;

    public ActivationToken() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public LocalDateTime getTokenTimeLimit() {
        return tokenTimeLimit;
    }

    public void setTokenTimeLimit(LocalDateTime tokenTimeLimit) {
        this.tokenTimeLimit = tokenTimeLimit;
    }
}
