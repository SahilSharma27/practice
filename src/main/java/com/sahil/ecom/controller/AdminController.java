package com.sahil.ecom.controller;

import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.ResponseDTO;
import com.sahil.ecom.dto.category.AddCategoryDTO;
import com.sahil.ecom.dto.category.CategoryUpdateDTO;
import com.sahil.ecom.dto.category.metadata.field.AddMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.value.AddCategoryMetaDataFieldValueDTO;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.security.JwtUtil;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.*;
import com.sahil.ecom.service.impl.EmailSenderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;


@RestController
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final EmailSenderService emailSenderService;
    private final SellerService sellerService;
    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final LoginService loginService;
    private final CategoryService categoryService;
    private final JwtUtil jwtUtil;

    @PostMapping(value = "/login", params = "role=admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {

        //remove all tokens in db for this user
        //generate new tokens
        //save new tokens in db

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return ResponseEntity.ok(loginResponseDTO);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<Iterable<User>>(userService.getAllUsers(), HttpStatus.OK);
    }


    @GetMapping(value = "/users/customers")
    public ResponseEntity<?> getAllCustomers(HttpServletRequest request) {

        String email = request.getParameter("email");

        if (email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new ResponseEntity<>(userService.getAllCustomersPaged(page, size, sort), HttpStatus.OK);

        } else {

//            return ResponseEntity.ok(customerService.fetchCustomerProfileDetails(email));
            return ResponseEntity.ok(userService.getCustomer(email));

        }

    }

    @GetMapping(value = "/users/sellers")
    public ResponseEntity<?> getAllSellers(HttpServletRequest request) {

        String email = request.getParameter("email");
        if (email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new ResponseEntity<>(userService.getAllSellersPaged(page, size, sort), HttpStatus.OK);
        } else {
//            return ResponseEntity.ok(sellerService.fetchSellerProfileDetails(email));
            return ResponseEntity.ok(userService.getSeller(email));
        }

    }


    @PatchMapping(value = "users/activate/{id}")
    public ResponseEntity<?> activateUserAccount(@PathVariable(name = "id") Long userId) {

        String message;

        if (userService.activateAccount(userId)) {

            message = messageSource.getMessage("user.account.activated", null, "message", LocaleContextHolder.getLocale());
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));

        }
        message = messageSource.getMessage("user.not.found", null, "message", LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(), false, message, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }


    @PatchMapping(value = "users/deactivate/{id}")
    public ResponseEntity<?> deActivateUserAccount(@PathVariable(name = "id") Long userId) {

        String message;

        if (userService.deActivateAccount(userId)) {
            message = messageSource.getMessage("user.account.deactivated", null, "message", LocaleContextHolder.getLocale());
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));

        }

        message = messageSource.getMessage("user.not.found", null, "message", LocaleContextHolder.getLocale());
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(), false, message, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/category/metadata")
    public ResponseEntity<?> addMetaDataField(@Valid @RequestBody AddMetaDataFieldDTO addMetaDataFieldDTO) {

        AddMetaDataFieldDTO savedMetaDatField = categoryService.addCategoryMetadataField(addMetaDataFieldDTO);
        return ResponseEntity.ok(savedMetaDatField);

    }

//    @PutMapping(value = "/category/metadata-update")
//    public ResponseEntity<?> updateMetaDataField(@RequestBody AddMetaDataFieldDTO addMetaDataFieldDTO){
//
////        AddMetaDataFieldDTO savedMetaDatField = categoryService.addCategoryMetadataField(addMetaDataFieldDTO);
////        return ResponseEntity.ok(savedMetaDatField);
//
//
//    }


    @GetMapping(value = "/category/metadata")
    public ResponseEntity<?> fetchMetaDataField() {
        return ResponseEntity.ok(categoryService.getAllMetaDataFields());
    }


    @PostMapping(value = "/category")
    public ResponseEntity<?> addNewCategory(@Valid @RequestBody AddCategoryDTO addCategoryDTO) {

        return ResponseEntity.ok(categoryService.addCategory(addCategoryDTO));

    }

    @PutMapping(value = "/category-update")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());

        if (categoryService.updateCategory(categoryUpdateDTO)) {

            responseDTO.setSuccess(true);
            responseDTO.setMessage(messageSource
                    .getMessage("category.update.successful", null, "message", LocaleContextHolder.getLocale()));
            responseDTO.setResponseStatusCode(HttpStatus.OK);

            return ResponseEntity.ok(responseDTO);
        }

        responseDTO.setSuccess(false);
        responseDTO.setMessage(messageSource
                .getMessage("category.update.not.successful", null, "message", LocaleContextHolder.getLocale()));
        responseDTO.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //,params = "role=admin"
    @GetMapping(value = "/categories", params = "role=admin")
    public ResponseEntity<?> fetchAllCategories(HttpServletRequest request) {
//check for admin role
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


        if (role.equals("ROLE_ADMIN")) {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } else
            throw new InvalidTokenException();

//        return ResponseEntity.ok(categoryService.getAllCategories());

    }

    @GetMapping(value = "/category/{category_id}")
    public ResponseEntity<?> fetchCategoryById(@PathVariable("category_id") Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping(value = "/category/metadata/value")
    public ResponseEntity<?> addCategoryMetaData(@Valid @RequestBody AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO) {

        categoryService.addCategoryMetadataFieldWithValue(addCategoryMetaDataFieldValueDTO);

        String message = messageSource.getMessage("category.metadata.field.value.added", null, "message", LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
    }


    @PutMapping(value = "/category/metadata/value-update")
    public ResponseEntity<?> updateCategoryMetaData(@Valid @RequestBody AddCategoryMetaDataFieldValueDTO updateCategoryMetaDataFieldValueDTO) {

        categoryService.updateCategoryMetadataFieldWithValue(updateCategoryMetaDataFieldValueDTO);
        String message = messageSource.getMessage("category.metadata.field.value.updated", null, "message", LocaleContextHolder.getLocale());

        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));

    }


}
