package com.sahil.ecom.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "blacklist_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistToken {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "access_token")
    private String accessToken;

    public BlacklistToken(String accessToken) {
        this.accessToken = accessToken;
    }
}


