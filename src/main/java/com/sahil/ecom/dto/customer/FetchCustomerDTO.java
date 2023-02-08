package com.sahil.ecom.dto.customer;

import com.sahil.ecom.entity.Customer;

public class FetchCustomerDTO {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;

    public FetchCustomerDTO() {
    }

    public FetchCustomerDTO(Customer customer) {

        this.setEmail(customer.getEmail());
        this.setId(customer.getId());
        this.setFullName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
//        this.setContact(customer.getContact());
        this.setActive(customer.isActive());

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
//    public String getContact() {
//        return contact;
//    }
//
//    public void setContact(String contact) {
//        this.contact = contact;
//    }
}
