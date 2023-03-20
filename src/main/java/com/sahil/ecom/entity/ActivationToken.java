package com.sahil.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activation_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivationToken {

    @Column(name = "token")
    @Id
    private String token;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "token_time_limit")
    private LocalDateTime tokenTimeLimit;
}
