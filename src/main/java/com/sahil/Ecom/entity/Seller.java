package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SELLER")
public class Seller extends User{

    @Column(name = "GST",unique = true)
    private String gst;

    @Column(name = "COMPANY_CONTACT", unique = true)
    private String companyContact;

    @Column(name = "COMPANY_NAME",unique = true)
    private String companyName;

    @JsonIgnore
    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public Seller() {
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProducts(Product product) {
        this.products.add(product);
    }
}
