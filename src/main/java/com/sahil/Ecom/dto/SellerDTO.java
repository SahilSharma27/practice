package com.sahil.Ecom.dto;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class SellerDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private  String firstName;

    private  String middleName;

    @NotBlank
    private  String lastName;

    @NotBlank
    @Size(min = 8,max = 15,message = "Password should be 8-15 characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String gst;

    @NotBlank
    @Size(min = 10,max = 10,message = "Contact number must be of 10 digits")
    private String companyContact;

    @NotBlank
    private String companyName;


    @NotNull
    @Valid
    AddressDTO address;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }
}
