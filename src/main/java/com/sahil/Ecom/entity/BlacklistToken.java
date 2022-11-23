package com.sahil.Ecom.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "BLACKLIST_TOKEN")
public class BlacklistToken {

    @Id
    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

//    private String userEmail;


    public BlacklistToken() {
    }

    public BlacklistToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}


