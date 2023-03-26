package com.sahil.ecom.dto;

import com.sahil.ecom.entity.Address;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddAddressDTO {

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String city;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String state;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String country;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String addressLine;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    @Size(min = 6,max = 6,message = "{zipcode.validation}")
    @Pattern(regexp = "^[0-9]+$",message ="{zipcode.validation}" )
    private String zipCode;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String label;

    public static AddAddressDTO mapAddressToAddAddressDTO(Address address) {
        return AddAddressDTO.builder()
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .country(address.getCountry())
                .label(address.getLabel())
                .zipCode(address.getZipCode())
                .state(address.getState()).build();
    }

    public Address mapAddressDTOtoAddress(){
        Address sellerAddress = new Address();
        sellerAddress.setAddressLine(this.getAddressLine());
        sellerAddress.setCity(this.getCity());
        sellerAddress.setCountry(this.getCountry());
        sellerAddress.setLabel(this.getLabel());
        sellerAddress.setZipCode(this.getZipCode());
        sellerAddress.setState(this.getState());
        return sellerAddress;
    }

}
