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
public class FetchCustomerDTO {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;

    public FetchCustomerDTO(Customer customer) {

        this.setEmail(customer.getEmail());
        this.setId(customer.getId());
        this.setFullName(customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());
        this.setActive(customer.isActive());

    }

}
