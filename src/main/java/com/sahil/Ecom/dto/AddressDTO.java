package com.sahil.Ecom.dto;

import com.sahil.Ecom.entity.Address;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

public class AddressDTO {

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private String addressLine;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String label;

    public AddressDTO() {
    }

    public AddressDTO(Address address){

        this.setAddressLine(address.getAddressLine());
        this.setCity(address.getCity());
        this.setCountry(address.getCountry());
        this.setLabel(address.getLabel());
        this.setZipCode(address.getZipCode());
        this.setState(address.getState());

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

    public Address mapAddressDTOtoAddress(){

        Address sellerAddress = new Address();
        sellerAddress.setAddressLine(this.getAddressLine());
        sellerAddress.setCity(this.getCity());
        sellerAddress.setCountry(this.getCountry());
        sellerAddress.setLabel(this.getLabel());
        sellerAddress.setZipCode(this.getZipCode());
        sellerAddress.setState(this.getState());

        return sellerAddress;

    }

}
