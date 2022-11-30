package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.User;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;


@RestController
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LoginService loginService;

    @Autowired
    private CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger(AdminController.class);

    Locale locale = LocaleContextHolder.getLocale();


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

        if(email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new ResponseEntity<>(userService.getAllCustomersPaged(page, size, sort), HttpStatus.OK);

        }else{

            return ResponseEntity.ok(customerService.fetchCustomerProfileDetails(email));

        }

    }

    @GetMapping(value = "/users/sellers")
    public ResponseEntity<?> getAllSellers(HttpServletRequest request) {

        String email = request.getParameter("email");
        if(email == null) {

            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new ResponseEntity<>(userService.getAllSellersPaged(page, size, sort), HttpStatus.OK);
        }else{
            return ResponseEntity.ok(sellerService.fetchSellerProfileDetails(email));
        }

    }


    @PatchMapping(value = "users/activate/{id}")
    public ResponseEntity<?> activateUserAccount(@PathVariable(name = "id") Long userId) {

        String message;

        if (userService.activateAccount(userId)) {

            message = messageSource.getMessage("user.account.activated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK));

        }
        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(),false,message,HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }


    @PatchMapping(value = "users/deactivate/{id}")
    public ResponseEntity<?> deActivateUserAccount(@PathVariable(name = "id") Long userId) {

        String message;

        if (userService.deActivateAccount(userId)){
            message = messageSource.getMessage("user.account.deactivated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true, message, HttpStatus.OK));

        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        return new ResponseEntity<>(new ResponseDTO(LocalDateTime.now(),false,message,HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/category/metadata")
    public ResponseEntity<?> addMetaDataField(@RequestBody AddMetaDataFieldDTO addMetaDataFieldDTO){

        AddMetaDataFieldDTO savedMetaDatField = categoryService.addCategoryMetadataField(addMetaDataFieldDTO);
        return ResponseEntity.ok(savedMetaDatField);

    }

    @GetMapping(value = "/category/metadata")
    public ResponseEntity<?> fetchMetaDataField(){
        return ResponseEntity.ok(categoryService.getAllMetaDataFields());
    }



    @PostMapping(value = "/category")
    public ResponseEntity<?> addNewCategory(@RequestBody AddCategoryDTO addCategoryDTO){

        AddCategoryDTO addedCategory = categoryService.addCategory(addCategoryDTO);
        return ResponseEntity.ok(addedCategory);

    }

    @GetMapping(value = "/category")
    public ResponseEntity<?>fetchAllCategories(){

        return ResponseEntity.ok(categoryService.getAllCategories());

    }


    @GetMapping(value = "/categoryy")
    public ResponseEntity<?>fetchCategoryById(@RequestParam("categoryId")Long categoryId){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

    @PostMapping(value = "/category/metadata/value")
    public ResponseEntity<?>addCategoryMetaData(@RequestBody AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO){

        categoryService.addCategoryMetadataFieldWithValue(addCategoryMetaDataFieldValueDTO);

        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,"added",HttpStatus.OK));

    }


}
