package com.sahil.ecom.controller;

import com.sahil.ecom.dto.LoginResponseDTO;
import com.sahil.ecom.dto.ResponseDTO;
import com.sahil.ecom.dto.password.ForgotPasswordDTO;
import com.sahil.ecom.dto.password.ResetPassDTO;
import com.sahil.ecom.entity.Address;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.BlacklistTokenRepository;
import com.sahil.ecom.security.JwtUtil;
import com.sahil.ecom.security.TokenGeneratorHelper;
import com.sahil.ecom.service.FileService;
import com.sahil.ecom.service.LoginService;
import com.sahil.ecom.service.UserService;
import com.sahil.ecom.service.impl.GeneralMailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final TokenGeneratorHelper tokenGeneratorHelper;
    private final UserDetailsService userDetailsService;
    private final MessageSource messageSource;
    private final GeneralMailService generalMailService;
    private final JwtUtil jwtUtil;
    private final LoginService loginService;
    private final BlacklistTokenRepository blacklistTokenRepository;
    private final FileService fileService;
    @Value("${project.image}")
    private String path;
    @Value("${project.image.product}")
    private String pathProduct;


    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

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

        return new ResponseEntity<>(new LoginResponseDTO(newAccessToken, jwtRefreshToken), HttpStatus.OK);
    }


    @GetMapping(value = "/users/activate")
    public ResponseEntity<?> activateAccount(@RequestParam(name = "token") String uuid) {

//        1 check token in db
//        2 check time limit
//        3 get email
//        4 update account is active

        String email = userService.validateActivationToken(uuid);

        if (userService.activateByEmail(email)) {
            String message = messageSource.getMessage("user.account.activated", null, "message", LocaleContextHolder.getLocale());
            //send email
            generalMailService.sendAccountActivationAck(email);
            //
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.not.activated", null, "message", LocaleContextHolder.getLocale()));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(value = "/users/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {

//        1 check Email in db
//        2 generate url
//        3 send mail

        ResponseDTO responseDTO;
        String message;

        if (userService.forgotPasswordHelper(forgotPasswordDTO.getUserEmail())) {
            message = messageSource.getMessage("reset.pass.email.sent", null, "message", LocaleContextHolder.getLocale());
            responseDTO = new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK);

            return ResponseEntity.ok(responseDTO);
        }

        message = messageSource.getMessage("email.not.sent", null, "message", LocaleContextHolder.getLocale());
        responseDTO = new ResponseDTO(LocalDateTime.now(), false, message, HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @PutMapping(value = "/users/resetPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestParam String token, @RequestBody ResetPassDTO resetPassDTO) {
//        1 check token in db
//        check time limit
//        2 get email
//        3 update account pass

        if (resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())) {
            String email = userService.validateResetPasswordToken(token);

            if (email.equals(resetPassDTO.getUserEmail())) {
                userService.resetPassword(resetPassDTO.getNewPassword());
            }

            //send mail
            generalMailService.sendPasswordUpdateAck();

            String message = messageSource.getMessage("user.password.updated", null, "message", LocaleContextHolder.getLocale());
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
        }

        throw new GenericException("Pass not matching with confirm pass");

    }


    @PatchMapping(value = "/users/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody ResetPassDTO resetPassDTO) throws Exception {

        if (resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword()) && (userService.resetPassword(resetPassDTO.getNewPassword()))) {
                generalMailService.sendPasswordUpdateAck();
                return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, "User Password Updated", HttpStatus.OK));
        }

        throw new GenericException("Pass not matching with confirm pass");

    }

    @GetMapping(value = "/users/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        userService.logoutHelper();
        responseDTO.setSuccess(true);
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        responseDTO.setMessage(messageSource.getMessage("user.logged.out", null, "message", LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(responseDTO);

    }

    //common for customer and seller
    @PatchMapping(value = "users/address/{address_id}")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, @PathVariable(name = "address_id") Long id) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        userService.updateAddress(id, address);
        responseDTO.setTimestamp(LocalDateTime.now());
        String message;
        message = messageSource.getMessage("user.address.updated", null, "message", LocaleContextHolder.getLocale());
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        responseDTO.setMessage(message);
        return ResponseEntity.ok(responseDTO);

    }

    @PostMapping(value = "users/image/{user_id}")
    public ResponseEntity<?> upload(@PathVariable(name = "user_id") Long id, @RequestParam("image") MultipartFile image) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());


        log.info("--------------------------" + image.getContentType());

        if (userService.saveUserImage(id, image)) {

            responseDTO.setResponseStatusCode(HttpStatus.OK);
            responseDTO.setMessage("Image uploaded Successfully");
            responseDTO.setSuccess(true);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }

        responseDTO.setSuccess(false);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);


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
