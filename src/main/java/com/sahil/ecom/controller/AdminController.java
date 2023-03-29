package com.sahil.ecom.controller;

import com.sahil.ecom.dto.LoginRequestDTO;
import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.category.AddCategoryDTO;
import com.sahil.ecom.dto.category.CategoryUpdateDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.category.SavedCategoryDTO;
import com.sahil.ecom.dto.category.metadata.field.AddMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.FetchMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.value.AddCategoryMetaDataFieldValueDTO;
import com.sahil.ecom.dto.customer.FetchCustomerDTO;
import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.dto.response.SuccessResponseDto;
import com.sahil.ecom.dto.seller.FetchSellerDTO;
import com.sahil.ecom.entity.User;
import com.sahil.ecom.enums.EcomRoles;
import com.sahil.ecom.exception.InvalidTokenException;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.CategoryService;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.util.EcomConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@RestController
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final MessageSource messageSource;
    private final LoginService loginService;
    private final CategoryService categoryService;


    /**
     * remove all tokens in db for this user
     * generate new tokens
     * save new tokens in db
     */
    @PostMapping(value = "/login", params = "role=admin")
    public ResponseDto<LoginResponseDTO> loginAdmin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO.getUsername());

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return new SuccessResponseDto<>(loginResponseDTO);
    }

    @GetMapping("/users")
    public ResponseDto<Iterable<User>> getAllUsers() {
        return new SuccessResponseDto<>(userService.getAllUsers());
    }


    @GetMapping(value = "/users/customers")
    public ResponseDto<List<FetchCustomerDTO>> getAllCustomers(HttpServletRequest request) {

        String email = request.getParameter("email");
        if (Objects.isNull(email)) {
            int page = (request.getParameter("page") == null) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = (request.getParameter("size") == null) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = (request.getParameter("sort") == null) ? "id" : request.getParameter("sort");

            return new SuccessResponseDto<>(userService.getAllCustomersPaged(page, size, sort));

        } else {
            return new SuccessResponseDto<>(List.of(userService.getCustomer(email)));
        }

    }

    @GetMapping(value = "/users/sellers")
    public ResponseDto<List<FetchSellerDTO>> getAllSellers(HttpServletRequest request) {

        String email = request.getParameter("email");
        if (Objects.isNull(email)) {
            int page = Objects.isNull(request.getParameter("page")) ? 0 : Integer.parseInt(request.getParameter("page"));
            int size = Objects.isNull(request.getParameter("size")) ? 10 : Integer.parseInt(request.getParameter("size"));
            String sort = Objects.isNull(request.getParameter("sort")) ? "id" : request.getParameter("sort");

            return new SuccessResponseDto<>(userService.getAllSellersPaged(page, size, sort));
        } else {
            return new SuccessResponseDto<>(List.of(userService.getSeller(email)));
        }

    }


    @PatchMapping(value = "users/activate/{id}")
    public ResponseDto<Boolean> activateUserAccount(@PathVariable(name = "id") Long userId) {
        return new SuccessResponseDto<>(userService.activateAccount(userId), messageSource.getMessage("user.account.activated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @PatchMapping(value = "users/deactivate/{id}")
    public ResponseDto<Boolean> deActivateUserAccount(@PathVariable(name = "id") Long userId) {
        return new SuccessResponseDto<>(userService.deActivateAccount(userId), messageSource.getMessage("user.account.deactivated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }

    @PostMapping(value = "/category/metadata")
    public ResponseDto<AddMetaDataFieldDTO> addMetaDataField(@Valid @RequestBody AddMetaDataFieldDTO addMetaDataFieldDTO) {
        return new SuccessResponseDto<>(categoryService.addCategoryMetadataField(addMetaDataFieldDTO));
    }

    /**
     * @PutMapping(value = "/category/metadata-update")
     * public ResponseEntity<?> updateMetaDataField(@RequestBody AddMetaDataFieldDTO addMetaDataFieldDTO){
     * AddMetaDataFieldDTO savedMetaDatField = categoryService.addCategoryMetadataField(addMetaDataFieldDTO);
     * return ResponseEntity.ok(savedMetaDatField);
     * }
     */


    @GetMapping(value = "/category/metadata")
    public ResponseDto<List<FetchMetaDataFieldDTO>> fetchMetaDataField() {
        return new SuccessResponseDto<>(categoryService.getAllMetaDataFields());
    }


    @PostMapping(value = "/category")
    public ResponseDto<SavedCategoryDTO> addNewCategory(@Valid @RequestBody AddCategoryDTO addCategoryDTO) {
        return new SuccessResponseDto<>(categoryService.addCategory(addCategoryDTO));
    }

    @PutMapping(value = "/category-update")
    public ResponseDto<Boolean> updateCategory(@Valid @RequestBody CategoryUpdateDTO categoryUpdateDTO) {
        return new SuccessResponseDto<>(categoryService.updateCategory(categoryUpdateDTO), messageSource.getMessage("category.update.successful", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }

    @GetMapping(value = "/categories", params = "role=admin")
    public ResponseDto<List<FetchCategoryDTO>> fetchAllCategories() {
        if (userService.getRole().equals(EcomRoles.ADMIN.role)) {
            return new SuccessResponseDto<>(categoryService.getAllCategories());
        } else throw new InvalidTokenException();
    }

    @GetMapping(value = "/category/{category_id}")
    public ResponseDto<FetchCategoryDTO> fetchCategoryById(@PathVariable("category_id") Long categoryId) {
        return new SuccessResponseDto<>(categoryService.getCategoryById(categoryId));
    }

    @PostMapping(value = "/category/metadata/value")
    public ResponseDto<Boolean> addCategoryMetaData(@Valid @RequestBody AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO) {
        categoryService.addCategoryMetadataFieldWithValue(addCategoryMetaDataFieldValueDTO);
        return new SuccessResponseDto<>(true, messageSource.getMessage("category.metadata.field.value.added", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @PutMapping(value = "/category/metadata/value-update")
    public ResponseDto<Boolean> updateCategoryMetaData(@Valid @RequestBody AddCategoryMetaDataFieldValueDTO updateCategoryMetaDataFieldValueDTO) {
        categoryService.updateCategoryMetadataFieldWithValue(updateCategoryMetaDataFieldValueDTO);
        return new SuccessResponseDto<>(true, messageSource.getMessage("category.metadata.field.value.updated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


}
