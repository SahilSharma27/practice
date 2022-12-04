package com.sahil.Ecom.entity;


import com.sahil.Ecom.audit.Auditable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROLE")
public class Role extends Auditable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "AUTHORITY")
    private String authority;


    @ManyToMany(mappedBy = "roles")
    List<User> users;


    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
