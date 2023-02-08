package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.ecom.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Entity
@SQLDelete(sql = "UPDATE ADDRESS SET IS_DELETED = 1 where ID = ?")
@Where(clause = "IS_DELETED = 0")
@Table(name = "ADDRESS")
@Getter
@Setter
@NoArgsConstructor
public class Address extends Auditable {

    @Column(name = "ID")
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
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
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @PrePersist
    public void setFlags() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

}
