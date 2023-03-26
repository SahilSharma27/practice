package com.sahil.ecom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.ResponseDTO;
import com.sahil.ecom.dto.product.AddProductDTO;
import com.sahil.ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.ecom.dto.seller.AddSellerDTO;
import com.sahil.ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.security.JwtUtil;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.ProductService;
import com.sahil.ecom.service.SellerService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.service.impl.GeneralMailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;


@RestController
@AllArgsConstructor
@Slf4j
public class SellerController {
    private final UserService userService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final SellerService sellerService;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;
    private final LoginService loginService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    private final GeneralMailService generalMailService;


    @PostMapping(value = "/register", params = "role=seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody AddSellerDTO addSellerDTO) {

        //check pass and cpass
        if (!addSellerDTO.getPassword().equals(addSellerDTO.getConfirmPassword()))
            throw new GenericException("Password not matching with confirm password");

        //unique email
        if (userService.checkUserEmail(addSellerDTO.getEmail())) {
            throw new GenericException("Email already registered");
        }

        //unique company name
        if (sellerService.checkSellerCompanyName(addSellerDTO.getCompanyName())) {
            throw new GenericException("Company name already registered");
        }

        //unique gst
        if (sellerService.checkSellerGst(addSellerDTO.getGst())) {
            throw new GenericException("GST already registered");
        }

//        1)save user
        if (sellerService.register(addSellerDTO)) {

            //send acknowledgment

//             userService.sendSellerAcknowledgement(addSellerDTO.getEmail());

            generalMailService.sendAccountRegistrationAckForSeller(addSellerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", LocaleContextHolder.getLocale()));

            return ResponseEntity.ok(responseDTO);

        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping(value = "/login", params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {


        LoginResponseDTO loginResponseDTO = sellerService.loginSeller(loginRequestDTO);
        if (loginResponseDTO != null) {
            return ResponseEntity.ok(loginResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("login.failed", null, "message", LocaleContextHolder.getLocale()));
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @GetMapping(value = "users/profile", params = "role=seller")
    public ResponseEntity<?> getSellerProfile(HttpServletRequest request) {
        //get token
        //get username
        //get profile

        String requestHeader = request.getHeader("Authorization");

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
            String accessToken = requestHeader.substring("Bearer ".length());

            try {

                String userEmail = this.jwtUtil.extractUsername(accessToken);
                return ResponseEntity.ok(sellerService.fetchSellerProfileDetails(userEmail));

            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }
        }
        throw new InvalidTokenException();

    }

    @PatchMapping(value = "/users/update/profile", params = "role=seller")
    public ResponseEntity<?> updateProfile(@RequestBody SellerProfileUpdateDTO sellerProfileUpdateDTO, HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            username = jwtUtil.extractUsername(accessToken);

            sellerService.updateSellerProfile(username, sellerProfileUpdateDTO);
            String message = messageSource.getMessage("user.profile.updated", null, "message", LocaleContextHolder.getLocale());

            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
        }

        throw new InvalidTokenException();
    }

    @GetMapping(value = "/categories", params = "role=seller")
    public ResponseEntity<?> fetchAllCategoriesForSeller(HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);

            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidTokenException();
            }

        }

        String role = userService.getRole(username);
        log.info("------------------" + role + "-------------------");

        if (role.equals("ROLE_SELLER")) {
            return ResponseEntity.ok(sellerService.getAllCategoriesForSeller());
        } else
            throw new InvalidTokenException();


//        return ResponseEntity.ok(sellerService.getAllCategoriesForSeller());

    }


    @PostMapping(value = "/products")
    public ResponseEntity<?> addProduct(@RequestBody AddProductDTO addProductDTO, HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

        }

        productService.addProduct(addProductDTO, username);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setSuccess(true);
        responseDTO.setMessage("PRODUCT ADDED SUCCESSFULLY");
        responseDTO.setResponseStatusCode(HttpStatus.OK);

        return ResponseEntity.ok(responseDTO);

    }


//    @PostMapping(value = "/products/variation")
//    public ResponseEntity<?>addProductVariation(@RequestBody AddProductVariationDTO addProductVariationDTO, HttpServletRequest request){
//
//        productService.addProductVariation(addProductVariationDTO, file);
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
    public ResponseEntity<?> addProductVariationWithFile(
            @RequestParam("primary_image") MultipartFile file,
            @RequestParam("product_variation") String productVariation) {

        log.info(file.getOriginalFilename());
        log.info(productVariation);
        AddProductVariationDTO addProductVariationDTO;
        try {
            addProductVariationDTO =
                    objectMapper.readValue(productVariation, AddProductVariationDTO.class);

        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("invalid request", HttpStatus.BAD_REQUEST);
        }

        productService.addProductVariation(addProductVariationDTO, file);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setSuccess(true);
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        responseDTO.setMessage("PRODUCT VARIATION ADDED SUCCESSFULLY image uploaded");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/products", params = "role=seller")
    public ResponseEntity<?> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            HttpServletRequest servletRequest) {

        String requestHeader = servletRequest.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

        }


        return ResponseEntity.ok(
                productService
                        .getAllProductsForSeller(username
                                , Integer.parseInt(page)
                                , Integer.parseInt(size)
                                , sort
                                , order));

    }


    @GetMapping(value = "/products/{product_id}", params = "role=seller")
    public ResponseEntity<?> getProduct(@PathVariable("product_id") Long productId, HttpServletRequest servletRequest) {

        String requestHeader = servletRequest.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

        }

        return ResponseEntity.ok(
                productService
                        .getProductForSeller(username, productId));

    }

    @GetMapping(value = "/products/variation/{product_variation_id}", params = "role=seller")
    public ResponseEntity<?> getProductVariation(@PathVariable("product_variation_id") Long id, HttpServletRequest servletRequest) {

        String requestHeader = servletRequest.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

        }

        return ResponseEntity.ok(
                productService
                        .getProductVariationForSeller(username, id));

    }

    @GetMapping(value = "/products/variation", params = "role=seller")
    public ResponseEntity<?> getAllProductVariation(
            @RequestParam(value = "page", defaultValue = "0") String page,
            @RequestParam(value = "size", defaultValue = "10") String size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam("product_id") String id, HttpServletRequest servletRequest) {

        String requestHeader = servletRequest.getHeader("Authorization");
        Long productId = Long.parseLong(id);

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

        }

        return ResponseEntity.ok(productService
                .getAllProductVariationsForSeller(
                        username, productId
                        , Integer.parseInt(page)
                        , Integer.parseInt(size)
                        , sort
                        , order));
    }


    @PutMapping("/product/activate")
    public ResponseEntity<?> activateProduct(@RequestParam("product_id") String id, HttpServletRequest request) {
        String requestHeader = request.getHeader("Authorization");

        Long productId = Long.parseLong(id);

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            } catch (Exception e) {
                throw new InvalidTokenException();
            }

            productService.activateProduct(username, productId);

            ResponseDTO responseDTO = new ResponseDTO();
            responseDTO.setMessage("PRODUCT ACTIVATED");
            responseDTO.setSuccess(true);
            responseDTO.setResponseStatusCode(HttpStatus.OK);
            return ResponseEntity.ok(responseDTO);

        }

        throw new InvalidTokenException();


    }

}
