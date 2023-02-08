package com.sahil.ecom.dto.seller;

import com.sahil.ecom.dto.AddAddressDTO;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class AddSellerDTO {

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",message = "{email.validation}")
//            "The following restrictions are imposed in the email address' local part by using this regex:\n" +
//            "\n" +
//            "    It allows numeric values from 0 to 9.\n" +
//            "    Both uppercase and lowercase letters from a to z are allowed.\n" +
//            "    Allowed are underscore “_”, hyphen “-“, and dot “.”\n" +
//            "    Dot isn't allowed at the start and end of the local part.\n" +
//            "    Consecutive dots aren't allowed.\n" +
//            "    For the local part, a maximum of 64 characters are allowed.\n"
    private String email;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private  String firstName;


    private  String middleName;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private  String lastName;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Size(min = 8,max = 15,message ="{password.validation}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$",
            message = "8-15 Characters with atLeast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number")
    private String password;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String confirmPassword;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Size(min = 15,max = 15,message = "{gst.validation}")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message ="{gst.validation}")
//                    "GST should be 15 characters long.\n" +
//                            "The first 2 characters should be a number.\n"+
//                            "The next 10 characters should be the PAN number of the taxpayer.\n" +
//                            "The 13th character (entity code) should be a number from 1-9 or an alphabet.\n" +
//                            "The 14th character should be Z.\n" +
//                            "The 15th character should be an alphabet or a number."
    private String gst;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Size(min = 10,max = 10,message = "{contact.validation}")
    @Pattern(regexp = "^[0-9]+$",message ="{contact.validation}" )
    private String companyContact;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String companyName;

    @NotNull(message = "{not.null}")
    @Valid
    AddAddressDTO address;

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

    public AddAddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddAddressDTO address) {
        this.address = address;
    }



}
