package com.sahil.ecom.dto.address;

import com.sahil.ecom.entity.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchAddressDTO {

    private Long id;

    private String city;

    private String state;

    private String country;

    private String addressLine;

    private String zipCode;

    private String label;

    public FetchAddressDTO(Address address) {
        this.setId(address.getId());
        this.setAddressLine(address.getAddressLine());
        this.setCity(address.getCity());
        this.setCountry(address.getCountry());
        this.setLabel(address.getLabel());
        this.setZipCode(address.getZipCode());
        this.setState(address.getState());
    }
}
