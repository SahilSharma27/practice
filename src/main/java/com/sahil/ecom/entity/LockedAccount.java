package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.ecom.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "locked_account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LockedAccount extends Auditable {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(name = "locked_time")
    LocalDateTime lockedTime;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
