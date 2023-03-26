package com.sahil.ecom.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfileUpdateDTO {

    private String firstName;
    private String middleName;
    private String lastName;
    private String companyContact;
    private String companyName;
    private String gst;

}
