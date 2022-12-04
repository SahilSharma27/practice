package com.sahil.Ecom.dto.customer;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerProfileUpdateDTO {


    private String firstName;

    private String middleName;

    private String lastName;

    @Size(max = 10,min = 10,message = "{contact.validation}")
    @Pattern(regexp = "^[0-9]+$",message ="{contact.validation}" )
    private String contact;

    public CustomerProfileUpdateDTO() {

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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
