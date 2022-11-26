package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "JWT_REFRESH_TOKEN")
public class JwtRefreshToken {

    @Id
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;
//
//    @OneToMany(mappedBy = "jwtRefreshToken",cascade = CascadeType.ALL)
//    private List<JwtAccessToken> jwtAccessToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

    public JwtRefreshToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public List<JwtAccessToken> getJwtAccessToken() {
//        return jwtAccessToken;
//    }
//
//    public void setJwtAccessToken(List<JwtAccessToken> jwtAccessToken) {
//        this.jwtAccessToken = jwtAccessToken;
//    }
}
