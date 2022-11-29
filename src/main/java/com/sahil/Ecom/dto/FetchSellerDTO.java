package com.sahil.Ecom.dto;

import com.sahil.Ecom.entity.Seller;

//For admin
public class FetchSellerDTO {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;
    private String companyName;
    private AddressDTO companyAddress;
    private String companyContact;

    private String gst;

    public FetchSellerDTO() {

    }

    public FetchSellerDTO(Seller seller) {

        this.setEmail(seller.getEmail());
        this.setId(seller.getId());
        this.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
        this.setCompanyName(seller.getCompanyName());
        this.setCompanyContact(seller.getCompanyContact());
        this.setGst(seller.getCompanyContact());
        this.setActive(seller.isActive());
        this.setCompanyAddress(seller.getAddresses().stream().map(AddressDTO::new).toList().get(0));

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public AddressDTO getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(AddressDTO companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }
}
