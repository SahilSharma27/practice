package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.Ecom.audit.Auditable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JWT_ACCESS_TOKEN")
public class JwtAccessToken {
//
    @Id
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "ACCESS_TOKEN")
    private String accessToken;

//    @ManyToOne
//    @JoinColumn(name = "REFRESH_TOKEN_ID", referencedColumnName = "REFRESH_TOKEN")
//    JwtRefreshToken jwtRefreshToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    public JwtAccessToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}
