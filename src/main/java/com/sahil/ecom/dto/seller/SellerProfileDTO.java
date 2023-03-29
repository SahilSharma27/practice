package com.sahil.ecom.dto.seller;

import com.sahil.ecom.dto.address.FetchAddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerProfileDTO {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String companyContact;
    private String companyName;
    private String imageUrl;
    private boolean isActive;
    private String email;
    private String gst;
    private FetchAddressDTO companyAddress;

}
