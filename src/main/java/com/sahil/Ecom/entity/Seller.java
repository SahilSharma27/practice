package com.sahil.Ecom.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "SELLER")
public class Seller extends User{

    @Column(name = "GST")
    @NotNull
    private String gst;

    @Column(name = "COMPANY_CONTACT")
    @NotNull
    private String companyContact;

    @Column(name = "COMPANY_NAME")
    @NotNull
    private String companyName;

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

}
