package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.ecom.audit.Auditable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Entity
@SQLDelete(sql = "UPDATE address SET is_deleted = 1 where id = ?")
@Where(clause = "is_deleted = 0")
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address extends Auditable {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "addressLine")
    private String addressLine;

    @Column(name = "zip_code")
    private String zipCode;

    /***(Ex. office/home)*/

    @Column(name = "label")
    private String label;

    @JsonIgnore
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @PrePersist
    public void setFlags() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }

}
