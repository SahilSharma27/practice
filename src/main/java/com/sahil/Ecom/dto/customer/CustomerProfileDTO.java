package com.sahil.Ecom.dto.customer;

import com.sahil.Ecom.entity.Customer;

public class CustomerProfileDTO {


    private String firstName;
    private String lastName;
    private String contact;
    private String imageUrl;
    private boolean isActive;


    public CustomerProfileDTO() {
    }

    public CustomerProfileDTO(Customer customer) {
        this.firstName=customer.getFirstName();
        this.lastName=customer.getLastName();
        this.isActive = customer.isActive();
        this.contact = customer.getContact();
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
