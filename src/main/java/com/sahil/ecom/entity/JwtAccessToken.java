package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "jwt_acces_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAccessToken {
    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
