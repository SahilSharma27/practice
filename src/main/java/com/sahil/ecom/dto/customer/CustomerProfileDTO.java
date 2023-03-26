package com.sahil.ecom.dto.customer;

import com.sahil.ecom.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfileDTO {

    private String firstName;
    private String lastName;
    private String contact;
    private String imageUrl;
    private boolean isActive;

    public CustomerProfileDTO(Customer customer) {
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.isActive = customer.isActive();
        this.contact = customer.getContact();
    }
}
