package com.sahil.Ecom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.dto.seller.SellerDTO;
import com.sahil.Ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.Ecom.exception.*;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.service.LoginService;
import com.sahil.Ecom.service.ProductService;
import com.sahil.Ecom.service.SellerService;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;


@RestController
public class SellerController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LoginService loginService;


    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    Logger logger = LoggerFactory.getLogger(SellerController.class);

    Locale locale = LocaleContextHolder.getLocale();

    @PostMapping(value = "/register", params = "role=seller")
    public ResponseEntity<?> registerSeller(@Valid @RequestBody SellerDTO sellerDTO) {

        Locale locale = LocaleContextHolder.getLocale();

        //check pass and cpass
        if (!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword()))
            throw new PassConfirmPassNotMatchingException();

        //unique email
        if (userService.checkUserEmail(sellerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        //unique company name
        if (sellerService.checkSellerCompanyName(sellerDTO.getCompanyName())) {
            throw new CompanyNameAlreadyRegisteredException();
        }

        //unique gst
        if (sellerService.checkSellerGst(sellerDTO.getGst())) {
            throw new GstAlreadyRegisteredException();
        }

//        1)save user
        if (sellerService.register(sellerDTO)) {

            //send acknowledgment
             userService.sendSellerAcknowledgement(sellerDTO.getEmail());

            ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), true, HttpStatus.OK);
            responseDTO.setMessage(messageSource.getMessage("user.registered.successful", null, "message", locale));

            return ResponseEntity.ok(responseDTO);

        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.registered.unsuccessful", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping(value = "/login", params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {


        LoginResponseDTO loginResponseDTO = sellerService.loginSeller(loginRequestDTO);
        if(loginResponseDTO!=null){
            return ResponseEntity.ok(loginResponseDTO);
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(),false,HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("login.failed",null,"message",locale));
        return new ResponseEntity<>(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);

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

    @PatchMapping(value = "/users/update/profile" ,params = "role=seller")
    public ResponseEntity<?> updateProfile(@RequestBody SellerProfileUpdateDTO sellerProfileUpdateDTO, HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            username = jwtUtil.extractUsername(accessToken);

            sellerService.updateSellerProfile(username,sellerProfileUpdateDTO);
            String message = messageSource.getMessage("user.profile.updated",null,"message",locale);

            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK));
        }

        throw new InvalidTokenException();
    }

    @GetMapping(value = "/category",params = "role=seller")
    public ResponseEntity<?>fetchAllCategoriesForSeller(){
        return ResponseEntity.ok(sellerService.getAllCategoriesForSeller());

    }


    @PostMapping(value = "/products")
    public ResponseEntity<?>addProduct(@RequestBody AddProductDTO addProductDTO,HttpServletRequest request){

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;

        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer ".length());
            try {
                username = jwtUtil.extractUsername(accessToken);
            }
            catch (Exception e){
                throw  new InvalidTokenException();
            }

        }

        productService.addProduct(addProductDTO,username);
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
    public ResponseEntity<?>addProductVariationWithFile(
            @RequestParam("primary_image") MultipartFile file,
            @RequestParam("product_variation") String productVariation){

        logger.info(file.getOriginalFilename());
        logger.info(productVariation);
        AddProductVariationDTO addProductVariationDTO;
        try {
            addProductVariationDTO =
                    objectMapper.readValue(productVariation, AddProductVariationDTO.class);

        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("invalid request",HttpStatus.BAD_REQUEST);
        }

        productService.addProductVariation(addProductVariationDTO,file);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        responseDTO.setSuccess(true);
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        responseDTO.setMessage("PRODUCT  VARIATION ADDED SUCCESSFULLY image uploaded");

        return ResponseEntity.ok(responseDTO);


    }

}
