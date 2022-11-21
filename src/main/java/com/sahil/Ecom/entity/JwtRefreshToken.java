package com.sahil.Ecom.entity;

import javax.persistence.*;

@Entity
@Table(name = "JWT_REFRESH_TOKEN")
public class JwtRefreshToken {

    @Id
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "USER_ID")
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
}
