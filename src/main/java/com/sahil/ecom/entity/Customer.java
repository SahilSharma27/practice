package com.sahil.ecom.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User{

    @Column(name = "contact")
    private String contact;

}
