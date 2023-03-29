package com.sahil.ecom.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressDTO {
    private String city;

    private String state;

    private String country;

    private String addressLine;

    private String zipCode;

    /***(Ex. office/home)*/
    private String label;

}
