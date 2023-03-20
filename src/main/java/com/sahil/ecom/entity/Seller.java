package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends User{

    @Column(name = "gst",unique = true)
    private String gst;

    @Column(name = "company_contact")
    private String companyContact;

    @Column(name = "comapany_name",unique = true)
    private String companyName;

    @JsonIgnore
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public void addProducts(Product product) {
        this.products.add(product);
    }
}
