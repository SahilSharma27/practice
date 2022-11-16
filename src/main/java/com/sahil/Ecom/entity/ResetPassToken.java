package com.sahil.Ecom.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "RESET_USERPASS_TOKEN")
public class ResetPassToken {
    @Column(name = "TOKEN")
    @Id
    private String resetPassToken;

    @Column(name = "EMAIL")
    private String email;

    public ResetPassToken() {
    }

    public String getResetPassToken() {
        return resetPassToken;
    }

    public void setResetPassToken(String resetPassToken) {
        this.resetPassToken = resetPassToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
