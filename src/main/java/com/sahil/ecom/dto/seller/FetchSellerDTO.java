package com.sahil.ecom.dto.seller;

import com.sahil.ecom.dto.FetchAddressDTO;
import com.sahil.ecom.entity.Seller;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchSellerDTO {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;
    private String companyName;
    private FetchAddressDTO companyAddress;
    private String companyContact;
    private String gst;

    public FetchSellerDTO(Seller seller) {

        this.setEmail(seller.getEmail());
        this.setId(seller.getId());
        this.setFullName(seller.getFirstName() + " " + seller.getMiddleName() + " " + seller.getLastName());
        this.setCompanyName(seller.getCompanyName());
        this.setCompanyContact(seller.getCompanyContact());
        this.setGst(seller.getCompanyContact());
        this.setActive(seller.isActive());
        this.setCompanyAddress(seller.getAddresses().stream().map(FetchAddressDTO::new).toList().get(0));

    }

}
