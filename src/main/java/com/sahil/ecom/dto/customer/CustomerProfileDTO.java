package com.sahil.ecom.dto.customer;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerProfileDTO {

    private String firstName;
    private String lastName;
    private String contact;
    private String imageUrl;
    private boolean isActive;

}
