package com.sahil.Ecom.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SELLER")
public class Seller extends User{

    @Column(name = "GST")
    private String gst;

    @Column(name = "COMPANY_CONTACT")
    private String companyContact;

    @Column(name = "COMPANY_NAME")
    private String companyName;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="USER_ID",referencedColumnName = "ID")
//    private Address address;

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
//
//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }
}
