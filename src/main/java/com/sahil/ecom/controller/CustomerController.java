package com.sahil.ecom.controller;


import com.sahil.ecom.dto.address.AddAddressDTO;
import com.sahil.ecom.dto.address.FetchAddressDTO;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.customer.AddCustomerDTO;
import com.sahil.ecom.dto.customer.CustomerProfileDTO;
import com.sahil.ecom.dto.customer.CustomerProfileUpdateDTO;
import com.sahil.ecom.dto.product.FetchProductCustomerDTO;
import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.dto.response.SuccessResponseDto;
import com.sahil.ecom.enums.EcomRoles;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.service.CustomerService;
import com.sahil.ecom.service.ProductService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.util.EcomConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {

    private final UserService userService;
    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final ProductService productService;

    @PostMapping(value = "/register", params = "role=customer")
    public ResponseDto<Boolean> registerCustomer(@Valid @RequestBody AddCustomerDTO addCustomerDTO) {
        return new SuccessResponseDto<>(customerService.register(addCustomerDTO));
    }


    @PostMapping(value = "/login", params = "role=customer")
    public ResponseDto<LoginResponseDTO> loginCustomer(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        log.info("Logging In..." + loginRequestDTO.getUsername());
        return new SuccessResponseDto<>(customerService.loginCustomer(loginRequestDTO));
    }


    @PostMapping(value = "/users/address", params = "role=customer")
    public ResponseDto<Boolean> addAddress(@Valid @RequestBody AddAddressDTO addAddressDTO) {
        return new SuccessResponseDto<>(customerService.addAddressToCustomer(addAddressDTO), messageSource.getMessage("customer.address.added", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @GetMapping(value = "/users/address", params = "role=customer")
    public ResponseDto<List<FetchAddressDTO>> getAddressList() {
        return new SuccessResponseDto<>(customerService.getAllCustomerAddresses());
    }

    @DeleteMapping(value = "/users/address/{addressId}", params = "role=customer")
    public ResponseDto<Boolean> deleteAddress(@PathVariable(name = "addressId") Long id) {
        return new SuccessResponseDto<>(customerService.removeAddress(id), messageSource.getMessage("address.deleted", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @GetMapping(value = "users/profile", params = "role=customer")
    public ResponseDto<CustomerProfileDTO> getCustomerProfile() {
        return new SuccessResponseDto<>(customerService.fetchCustomerProfileDetails());
    }

    @PatchMapping(value = "/users/update/profile", params = "role=customer")
    public ResponseDto<Boolean> updateCustomerProfile(@Valid @RequestBody CustomerProfileUpdateDTO customerProfileUpdateDTO) {
        return new SuccessResponseDto<>(customerService.updateProfile(customerProfileUpdateDTO), messageSource.getMessage("user.profile.updated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @GetMapping(value = "/categories", params = "role=customer")
    public ResponseDto<List<FetchCategoryDTO>> fetchAllCategoriesForCustomer(@RequestParam(name = "categoryId", required = false) Long categoryId) {
        if (userService.getRole().equals(EcomRoles.CUSTOMER.role)) {
            return new SuccessResponseDto<>(customerService.getAllCategoriesForCustomer(categoryId));
        } else
            throw new InvalidTokenException();
    }


    @GetMapping(value = "/products/{product_id}", params = "role=customer")
    public ResponseDto<FetchProductCustomerDTO> getProductForCustomer(@PathVariable("product_id") Long productId) {
        return new SuccessResponseDto<>(productService.getProductForCustomer(productId));
    }


    @GetMapping(value = "/products", params = "role=customer")
    public ResponseDto<List<FetchProductCustomerDTO>> getAllProductsCustomer(
            @RequestParam(value = "category_id") String categoryId,
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order) {
        return new SuccessResponseDto<>(productService.getAllProductsForCustomer(Long.parseLong(categoryId), Integer.parseInt(page), Integer.parseInt(size), sort, order));
    }

}
