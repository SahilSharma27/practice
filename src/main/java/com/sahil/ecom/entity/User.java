package com.sahil.ecom.entity;

import com.sahil.ecom.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class User extends Auditable {
    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "EMAIL", unique = true)
    @Email
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "IS_EXPIRED")
    private boolean isExpired;

    @Column(name = "IS_LOCKED")
    private boolean isLocked;

    @Column(name = "INVALID_ATTEMPT_COUNT")
    private int invalidAttemptCount;

    @Column(name = "PASSWORD_UPDATE_DATE")
    private Date passwordUpdateDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @OrderBy("label ASC")
    private List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private JwtAccessToken jwtAccessToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private JwtRefreshToken jwtRefreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LockedAccount lockedAccount;


}
