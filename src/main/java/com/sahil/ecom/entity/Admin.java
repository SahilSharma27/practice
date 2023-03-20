package com.sahil.ecom.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "admin")
@NoArgsConstructor
public class Admin extends User{

}
