package com.sahil.Ecom.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CUSTOMER")
public class Customer extends User{

    @Column(name = "CONTACT")
    private String contact;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name="USER_ID", referencedColumnName="ID")
//    List<Address> addressList;

    public Customer() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


//    public List<Address> getAddressList() {
//        return addressList;
//    }
//
//    public void setAddressList(List<Address> addressList) {
//        this.addressList = addressList;
//    }
}
