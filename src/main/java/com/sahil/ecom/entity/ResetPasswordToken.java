package com.sahil.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Entity
@Table(name = "reset_pass_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordToken {

    @Column(name = "token")
    @Id
    private String token;

    @Column(name = "email")
    private String userEmail;

    @Column(name  = "token_time_limit")
    private LocalDateTime tokenTimeLimit;
}
