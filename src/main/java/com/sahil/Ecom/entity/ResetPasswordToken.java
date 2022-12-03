package com.sahil.Ecom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "RESET_PASS_TOKEN")
public class ResetPasswordToken {

    @Column(name = "TOKEN")
    @Id
    private String resetPassToken;

    @Column(name = "EMAIL")
    private String userEmail;

    @Column(name  = "TOKEN_TIME_LIMIT")
    private LocalDateTime tokenTimeLimit;

    public ResetPasswordToken() {
    }

    public String getResetPassToken() {
        return resetPassToken;
    }

    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getTokenTimeLimit() {
        return tokenTimeLimit;
    }

    public void setTokenTimeLimit(LocalDateTime tokenTimeLimit) {
        this.tokenTimeLimit = tokenTimeLimit;
    }
}
