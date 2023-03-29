package com.sahil.ecom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.product.AddProductDTO;
import com.sahil.ecom.dto.product.FetchProductSellerDTO;
import com.sahil.ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.ecom.dto.product.variation.FetchProductVariationSellerDTO;
import com.sahil.ecom.dto.response.ErrorResponseDto;
import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.dto.response.SuccessResponseDto;
import com.sahil.ecom.dto.seller.AddSellerDTO;
import com.sahil.ecom.dto.seller.SellerProfileDTO;
import com.sahil.ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.ecom.enums.EcomRoles;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.service.ProductService;
import com.sahil.ecom.service.SellerService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.util.EcomConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
public class SellerController {
    private final UserService userService;
    private final SellerService sellerService;
    private final MessageSource messageSource;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/register", params = "role=seller")
    public ResponseDto<Boolean> registerSeller(@Valid @RequestBody AddSellerDTO addSellerDTO) {
        return new SuccessResponseDto<>(sellerService.register(addSellerDTO), messageSource.getMessage("user.registered.successful", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @PostMapping(value = "/login", params = "role=seller")
    public ResponseDto<LoginResponseDTO> loginSeller(@RequestBody LoginRequestDTO loginRequestDTO){
        return new SuccessResponseDto<>(sellerService.loginSeller(loginRequestDTO));
    }


    @GetMapping(value = "users/profile", params = "role=seller")
    public ResponseDto<SellerProfileDTO> getSellerProfile() {
        return new SuccessResponseDto<>(sellerService.fetchSellerProfileDetails());
    }

    @PatchMapping(value = "/users/update/profile", params = "role=seller")
    public ResponseDto<Boolean> updateProfile(@RequestBody SellerProfileUpdateDTO sellerProfileUpdateDTO) {
        return new SuccessResponseDto<>(sellerService.updateSellerProfile(sellerProfileUpdateDTO), messageSource.getMessage("user.profile.updated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }

    @GetMapping(value = "/categories", params = "role=seller")
    public ResponseDto<List<FetchCategoryDTO>> fetchAllCategoriesForSeller() {
        if (userService.getRole().equals(EcomRoles.SELLER.role)) {
            return new SuccessResponseDto<>(sellerService.getAllCategoriesForSeller());
        } else throw new InvalidTokenException();
    }


    @PostMapping(value = "/products")
    public ResponseDto<Boolean> addProduct(@RequestBody AddProductDTO addProductDTO) {
        return new SuccessResponseDto<>(productService.addProduct(addProductDTO), "PRODUCT ADDED SUCCESSFULLY");
    }


//    @PostMapping(value = "/products/variation")
//    public ResponseDto<?>addProductVariation(@RequestBody AddProductVariationDTO addProductVariationDTO, HttpServletRequest request){
//
//        return new SuccessResponseDto<>(productService.addProductVariation(addProductVariationDTO, file,);
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setTimestamp(LocalDateTime.now());
//        responseDTO.setSuccess(true);
//        responseDTO.setResponseStatusCode(HttpStatus.OK);
//        responseDTO.setMessage("PRODUCT  VARIATION ADDED SUCCESSFULLY");
//
//        return ResponseEntity.ok(responseDTO);
//
//    }

    @PostMapping(value = "/products/variation")
    public ResponseDto<Boolean> addProductVariationWithFile(@RequestParam("primary_image") MultipartFile file, @RequestParam("product_variation") String productVariation) {
        log.info(file.getOriginalFilename());
        log.info(productVariation);
        AddProductVariationDTO addProductVariationDTO;
        try {
            addProductVariationDTO = objectMapper.readValue(productVariation, AddProductVariationDTO.class);

        } catch (JsonProcessingException e) {
            return new ErrorResponseDto<>("invalid request", 400);
        }

        return new SuccessResponseDto<>(productService.addProductVariation(addProductVariationDTO, file), "PRODUCT VARIATION ADDED SUCCESSFULLY image uploaded");

    }

    @GetMapping(value = "/products", params = "role=seller")
    public ResponseDto<List<FetchProductSellerDTO>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") String page, @RequestParam(value = "size", defaultValue = "10") String size, @RequestParam(value = "sort", defaultValue = "id") String sort, @RequestParam(value = "order", defaultValue = "asc") String order) {
        return new SuccessResponseDto<>(productService.getAllProductsForSeller(Integer.parseInt(page), Integer.parseInt(size), sort, order));
    }


    @GetMapping(value = "/products/{product_id}", params = "role=seller")
    public ResponseDto<FetchProductSellerDTO> getProduct(@PathVariable("product_id") Long productId) {
        return new SuccessResponseDto<>(productService.getProductForSeller(productId));
    }

    @GetMapping(value = "/products/variation/{product_variation_id}", params = "role=seller")
    public ResponseDto<FetchProductVariationSellerDTO> getProductVariation(@PathVariable("product_variation_id") Long id) {
        return new SuccessResponseDto<>(productService.getProductVariationForSeller(id));
    }

    @GetMapping(value = "/products/variation", params = "role=seller")
    public ResponseDto<List<FetchProductVariationSellerDTO>> getAllProductVariation(@RequestParam(value = "page", defaultValue = "0") String page, @RequestParam(value = "size", defaultValue = "10") String size, @RequestParam(value = "sort", defaultValue = "id") String sort, @RequestParam(value = "order", defaultValue = "asc") String order, @RequestParam("product_id") String id) {
        return new SuccessResponseDto<>(productService.getAllProductVariationsForSeller(Long.valueOf(id), Integer.parseInt(page), Integer.parseInt(size), sort, order));
    }


    @PutMapping("/product/activate")
    public ResponseDto<Boolean> activateProduct(@RequestParam("product_id") String id) {
        return new SuccessResponseDto<>(productService.activateProduct(Long.valueOf(id)), "PRODUCT ACTIVATED");
    }

}
