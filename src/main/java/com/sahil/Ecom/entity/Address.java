package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.Ecom.audit.Auditable;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Entity
@SQLDelete(sql = "UPDATE ADDRESS SET IS_DELETED = 1 where ID = ?")
@Where(clause = "IS_DELETED = 0")
@Table(name = "ADDRESS")
public class Address extends Auditable {

    @Column(name="ID")
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CITY")
    private String city;

    @Column(name="STATE")
    private String state;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "ADDRESS_LINE")
    private String addressLine;

    @Column(name = "ZIP_CODE")
    private String zipCode;

//  (Ex. office/home)
    @Column(name = "LABEL")
    private String label;

    @JsonIgnore
    @Column(name="IS_DELETED",nullable = false)
    private Boolean isDeleted;



    @PrePersist
    public void setFlags(){
        if(isDeleted==null){
            isDeleted=false;
        }
    }


    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }


}
