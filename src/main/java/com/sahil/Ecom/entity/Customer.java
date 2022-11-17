package com.sahil.Ecom.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "CUSTOMER")
public class Customer extends User{

    @Column(name = "CONTACT")
    @NotNull
    private String contact;

    public Customer() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
