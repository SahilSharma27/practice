package com.sahil.ecom.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT_VARIATION")

public class ProductVariation {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "QUANTITY_AVAILABLE")
    private int quantityAvailable;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "METADATA")
    private JSONObject metadata;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;


    public ProductVariation() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
