package com.sahil.ecom.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends User{

    @Column(name = "CONTACT")
    private String contact;

}
