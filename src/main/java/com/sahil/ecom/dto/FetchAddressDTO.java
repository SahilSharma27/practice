package com.sahil.ecom.dto;

import com.sahil.ecom.entity.Address;

public class FetchAddressDTO {

    private Long id;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private String zipCode;

    private String label;

    public FetchAddressDTO() {
    }

    public FetchAddressDTO(Address address) {
        this.setId(address.getId());
        this.setAddressLine(address.getAddressLine());
        this.setCity(address.getCity());
        this.setCountry(address.getCountry());
        this.setLabel(address.getLabel());
        this.setZipCode(address.getZipCode());
        this.setState(address.getState());

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
}