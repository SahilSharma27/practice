package com.sahil.Ecom.dto;

import javax.validation.constraints.Email;

public class ForgotPasswordDTO {

    @Email
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
