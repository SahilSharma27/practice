package com.sahil.Ecom.entity;


import javax.persistence.*;


@Entity
@Table(name = "BLACKLIST_TOKEN")
public class BlacklistToken {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

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


