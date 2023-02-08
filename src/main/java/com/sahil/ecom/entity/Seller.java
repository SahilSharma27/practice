package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SELLER")
@Getter
@Setter
@NoArgsConstructor
public class Seller extends User{

    @Column(name = "GST",unique = true)
    private String gst;

    @Column(name = "COMPANY_CONTACT")
    private String companyContact;

    @Column(name = "COMPANY_NAME",unique = true)
    private String companyName;

    @JsonIgnore
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public void addProducts(Product product) {
        this.products.add(product);
    }
}
