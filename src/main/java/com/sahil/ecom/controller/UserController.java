package com.sahil.ecom.controller;

import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.address.UpdateAddressDTO;
import com.sahil.ecom.dto.password.ForgotPasswordDTO;
import com.sahil.ecom.dto.password.ResetPassDTO;
import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.dto.response.SuccessResponseDto;
import com.sahil.ecom.security.JwtUtil;
import com.sahil.ecom.service.FileService;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.util.EcomConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final MessageSource messageSource;
    private final JwtUtil jwtUtil;
    private final LoginService loginService;
    private final FileService fileService;
    @Value("${project.image}")
    private String path;
    @Value("${project.image.product}")
    private String pathProduct;


    @GetMapping("/token/refresh")
    public ResponseDto<LoginResponseDTO> refreshToken(HttpServletRequest request) {

        String refreshTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtRefreshToken = null;
        String newAccessToken = null;


        //check format
        if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer")) {
            jwtRefreshToken = refreshTokenHeader.substring("Bearer ".length());

            try {
                username = this.jwtUtil.extractUsername(jwtRefreshToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
//
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            newAccessToken = this.jwtUtil.generateToken(userDetails);

            //remove old token
            loginService.removeAlreadyGeneratedTokens(username);

            //save new tokens
            loginService.saveJwtResponse(new LoginResponseDTO(newAccessToken, jwtRefreshToken), username);


            log.info("------------------REFRESH________________");
            log.info(newAccessToken);

        }

        return new SuccessResponseDto<>(new LoginResponseDTO(newAccessToken, jwtRefreshToken));
    }

    /*
     *   1 check token in db
     *   2 check time limit
     *   3 get email
     *   4 update account is active
     */

    @GetMapping(value = "/users/activate")
    public ResponseDto<Boolean> activateAccount(@RequestParam(name = "token") String uuid) {
        return new SuccessResponseDto<>(userService.activateByEmail(uuid), messageSource.getMessage("user.account.activated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    /**
     * 1 check Email in db
     * 2 generate url
     * 3 send mail
     */

    @PostMapping(value = "/users/forgotPassword")
    public ResponseDto<Boolean> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        return new SuccessResponseDto<>(userService.forgotPasswordHelper(forgotPasswordDTO.getUserEmail()), messageSource.getMessage("reset.pass.email.sent", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    /**
     * 1 check token in db
     * check time limit
     * 2 get email
     * 3 update account pass
     */

    @PutMapping(value = "/users/resetPassword")
    public ResponseDto<Boolean> resetPassword(@Valid @RequestParam String token, @RequestBody ResetPassDTO resetPassDTO) {
        return new SuccessResponseDto<>(userService.validateAndResetPassword(token, resetPassDTO), messageSource.getMessage("user.password.updated", null, EcomConstants.DEFAULT_MESSAGE, LocaleContextHolder.getLocale()));
    }


    @PatchMapping(value = "/users/update/password")
    public ResponseDto<Boolean> updatePassword(@Valid @RequestBody ResetPassDTO resetPassDTO) {
        return new SuccessResponseDto<>(userService.validateAndUpdatePassword(resetPassDTO), "User Password Updated");
    }

    @GetMapping(value = "/users/logout")
    public ResponseDto<Boolean> logoutUser() {
        return new SuccessResponseDto<>(userService.logoutHelper(), messageSource.getMessage("user.logged.out", null, "message", LocaleContextHolder.getLocale()));
    }

    /**
     * common for customer and seller
     */
    @PatchMapping(value = "users/address/{address_id}")
    public ResponseDto<Boolean> updateAddress(@RequestBody UpdateAddressDTO address, @PathVariable(name = "address_id") Long id) {
        userService.updateAddress(id, address);
        return new SuccessResponseDto<>(true, messageSource.getMessage("user.address.updated", null, "message", LocaleContextHolder.getLocale()));
    }

    @PostMapping(value = "users/image/{user_id}")
    public ResponseDto<Boolean> upload(@PathVariable(name = "user_id") Long id, @RequestParam("image") MultipartFile image) {
        log.info("--------------------------" + image.getContentType());
        return new SuccessResponseDto<>(userService.saveUserImage(id, image), "Image uploaded Successfully");
    }

    @GetMapping(value = "/images/users/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveProfileImage(@PathVariable(name = "imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getImage(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }

    @GetMapping(value = "/images/product/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveProductImage(@PathVariable(name = "imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getImage(pathProduct, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
