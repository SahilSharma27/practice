package com.sahil.ecom.dto.customer;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileUpdateDTO {


    private String firstName;

    private String middleName;

    private String lastName;

    @Size(max = 10, min = 10, message = "{contact.validation}")
    @Pattern(regexp = "^[0-9]+$", message = "{contact.validation}")
    private String contact;

}
