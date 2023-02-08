package com.sahil.ecom.entity;

import com.sahil.ecom.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@Getter
@Setter
@NoArgsConstructor
public class Product extends Auditable {

    @Id
    @Column(name = "ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "IS_CANCELLABLE")
    private boolean isCancellable;

    @Column(name = "IS_RETURNABLE")
    private boolean isReturnable;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<ProductVariation> productVariations = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SELLER_ID")
    private Seller seller;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Product)
                &&
                (((Product) obj).getName().equals(this.getName()));

    }

    public int hashCode() {
        return this.name.hashCode();
    }


}
