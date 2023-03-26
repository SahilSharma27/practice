package com.sahil.ecom.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCustomerDTO {

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "{email.validation}")
    private String email;


    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String firstName;

    private String middleName;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String lastName;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,15}$",
            message = "{password.validation}")
    private String password;


    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String confirmPassword;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Size(max = 10, min = 10, message = "{contact.validation}")
    @Pattern(regexp = "^[0-9]+$", message = "{contact.validation}")
    private String contact;

}
