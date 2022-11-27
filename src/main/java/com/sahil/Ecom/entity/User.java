package com.sahil.Ecom.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL")
    @Email
    private String email;

    @Column(name = "FIRST_NAME")
    private  String firstName;

    @Column(name = "MIDDLE_NAME")
    private  String middleName;

    @Column(name = "LAST_NAME")
    private  String lastName;

    @Column(name = "PASSWORD")
    private String password;


    @Column(name = "IS_DELETED")
    private  boolean isDeleted;

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
    @JoinColumn(name="USER_ID", referencedColumnName="ID")
    List<Address> addresses;

    @ManyToMany(fetch = FetchType.EAGER)
            @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID",referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID",referencedColumnName = "ID"))
    List<Role> roles;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    JwtAccessToken jwtAccessToken;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    JwtRefreshToken jwtRefreshToken;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    LockedAccount lockedAccount;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getInvalidAttemptCount() {
        return invalidAttemptCount;
    }

    public void setInvalidAttemptCount(int invalidAttemptCount) {
        this.invalidAttemptCount = invalidAttemptCount;
    }

    public Date getPasswordUpdateDate() {
        return passwordUpdateDate;
    }

    public void setPasswordUpdateDate(Date passwordUpdateDate) {
        this.passwordUpdateDate = passwordUpdateDate;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public JwtAccessToken getJwtAccessToken() {
        return jwtAccessToken;
    }

    public void setJwtAccessToken(JwtAccessToken jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }

    public JwtRefreshToken getJwtRefreshToken() {
        return jwtRefreshToken;
    }

    public void setJwtRefreshToken(JwtRefreshToken jwtRefreshToken) {
        this.jwtRefreshToken = jwtRefreshToken;
    }

    public LockedAccount getLockedAccount() {
        return lockedAccount;
    }

    public void setLockedAccount(LockedAccount lockedAccount) {
        this.lockedAccount = lockedAccount;
    }
}
