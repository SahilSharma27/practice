package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.Ecom.audit.Auditable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LOCKED_ACCOUNT")
public class LockedAccount extends Auditable {

    @Id
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "LOCKED_TIME")
    LocalDateTime lockedTime;

    @OneToOne
    @MapsId
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User user;

//    public LockedAccount(LocalDateTime lockedTime, User user) {
//        this.lockedTime = lockedTime;
//        this.user = user;
//    }

    public LockedAccount() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLockedTime() {
        return lockedTime;
    }

    public void setLockedTime(LocalDateTime lockedTime) {
        this.lockedTime = lockedTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
