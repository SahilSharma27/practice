package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.ForgotPasswordDTO;
import com.sahil.Ecom.dto.ResetPassDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
import com.sahil.Ecom.exception.UserEmailNotFoundException;
import com.sahil.Ecom.repository.BlacklistTokenRepository;
import com.sahil.Ecom.security.JwtUtil;
import com.sahil.Ecom.security.TokenGeneratorHelper;
import com.sahil.Ecom.service.FileService;
import com.sahil.Ecom.service.LoginService;
import com.sahil.Ecom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    MessageSource messageSource;

    @Value("${project.image}")
    private String path;


    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    JwtUtil jwtUtil;


    @Autowired
    LoginService loginService;

    @Autowired
    BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    FileService fileService;


    Locale locale = LocaleContextHolder.getLocale();

    @GetMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtRefreshToken = null;
        String newAccessToken = null;


        //check format
        if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer")) {
            jwtRefreshToken = refreshTokenHeader.substring("Bearer".length());

            try {
                username = this.jwtUtil.extractUsername(jwtRefreshToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
//
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            newAccessToken = this.jwtUtil.generateToken(userDetails);

            logger.info("------------------REFRESH________________");
            logger.info(newAccessToken);

        }

        return new ResponseEntity<>(new LoginResponseDTO(newAccessToken, jwtRefreshToken), HttpStatus.OK);
    }


    @PostMapping(value = "/login", params = "role=admin")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws Exception {

        //remove all tokens in db for this user
        //generate new tokens
        //save new tokens in db

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO);

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

//        logger.info("token : " + jwtResponse);

        return ResponseEntity.ok(loginResponseDTO);
    }


//    @PostMapping(value = "/login", params = "role=customer")
//    public ResponseEntity<?> loginCustomer(@RequestBody JwtRequest jwtRequest) throws Exception {
//
//        loginService.removeAlreadyGeneratedTokens(jwtRequest);
//
//        JwtResponse jwtResponse = tokenGeneratorHelper.generateTokenHelper(jwtRequest);
//
//        loginService.saveJwtResponse(jwtResponse,jwtRequest.getUsername());
//
//        return ResponseEntity.ok(jwtResponse);
//    }


    @PostMapping(value = "/login", params = "role=seller")
    public ResponseEntity<?> loginSeller(@RequestBody LoginRequestDTO loginRequestDTO) throws Exception {

        loginService.removeAlreadyGeneratedTokens(loginRequestDTO);

        LoginResponseDTO loginResponseDTO = tokenGeneratorHelper.generateTokenHelper(loginRequestDTO);

        loginService.saveJwtResponse(loginResponseDTO, loginRequestDTO.getUsername());

        return ResponseEntity.ok(loginResponseDTO);
    }

    @GetMapping(value = "/users/activate")
    public ResponseEntity<?> activateAccount(@RequestParam(name = "token") String uuid) {

//        1 check token in db
//        2 check time limit
//        3 get email
//        4 update account is active

        String email = userService.validateActivationToken(uuid);

        userService.activateByEmail(email);

        String message = messageSource.getMessage("user.account.activated",null,"message",locale);
        return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK));

    }


    @PostMapping(value = "/users/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {

//        1 check Email in db
//        2 generate url
//        3 send mail

        if (userService.checkUserEmail(forgotPasswordDTO.getUserEmail())) {

            userService.forgotPasswordHelper(forgotPasswordDTO.getUserEmail());
            return new ResponseEntity<>("Reset Password link Sent", HttpStatus.OK);

        }

//        2)send reset pass link link

        throw new UserEmailNotFoundException(messageSource.getMessage("user.not.found", null, "message", locale));
        //return new ResponseEntity<>("USER ACCOUNT Not found",HttpStatus.NOT_FOUND);

    }

    @PostMapping(value = "/users/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPassDTO resetPassDTO) {
//        1 check token in db
//        check time limit
//        2 get email
//        3 update account pass

        logger.info("------------------------------------------------------------------");
        logger.info(resetPassDTO.getNewPassword() + "  " + resetPassDTO.getConfirmNewPassword());
        logger.info("------------------------------------------------------------------");

        if (resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())) {
            String email = userService.validateResetPasswordToken(token);

            if (email.equals(resetPassDTO.getUserEmail())) {
                userService.resetPassword(email, resetPassDTO.getNewPassword());
            }
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true,"User Password Updated", HttpStatus.OK));
        }

        throw new PassConfirmPassNotMatchingException();

    }


    @PatchMapping(value = "/users/update/password")
    public ResponseEntity<?> updatePassword(@RequestBody ResetPassDTO resetPassDTO, HttpServletRequest request) throws Exception {

        logger.info("------------------------------------------------------------------");
        logger.info(resetPassDTO.getNewPassword() + "  " + resetPassDTO.getConfirmNewPassword());
        logger.info("------------------------------------------------------------------");

        if (resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())) {
            String requestHeader = request.getHeader("Authorization");
            String username = null;
            String accessToken = null;

            //check format
            if (requestHeader != null && requestHeader.startsWith("Bearer")) {


                accessToken = requestHeader.substring("Bearer".length());
                try {
                    username = this.jwtUtil.extractUsername(accessToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (userService.resetPassword(username, resetPassDTO.getNewPassword()))
                    return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true,"User Password Updated", HttpStatus.OK));

                else throw new Exception("PTA NAHI");

            }
        }

        throw new PassConfirmPassNotMatchingException(messageSource.getMessage("password.confirm.password", null, "message", locale));
//


    }


    @GetMapping(value = "/users/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {

        String requestHeader = request.getHeader("Authorization");

        String username = null;
        String accessToken = null;


        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {


            accessToken = requestHeader.substring("Bearer".length());

            userService.logout(accessToken);

        }

        return new ResponseEntity<>("HELLO", HttpStatus.OK);
    }


    @PatchMapping(value = "users/address/{address_id}")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, @PathVariable(name = "address_id") Long id) {

        //find address by id
        //update given fields

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        String message;

        if (userService.updateAddress(id, address)) {
            message = messageSource.getMessage("user.address.updated", null, "message", locale);
            responseDTO.setResponseStatusCode(HttpStatus.OK);
            responseDTO.setMessage(message);
            return ResponseEntity.ok(responseDTO);
        }

        message = messageSource.getMessage("user.not.found", null, "message", locale);
        responseDTO.setResponseStatusCode(HttpStatus.NOT_FOUND);
        responseDTO.setMessage(message);

        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);

    }

    @PostMapping("users/image/{user_id}")
    public ResponseEntity<?> upload(@PathVariable(name = "user_id") Long id, @RequestParam("image") MultipartFile image) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());

        if (userService.saveUserImage(id, image)) {

            responseDTO.setResponseStatusCode(HttpStatus.OK);
            responseDTO.setMessage("Image uploaded Successfully");
            responseDTO.setSuccess(true);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }

        responseDTO.setSuccess(false);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//

    }

    @GetMapping(value = "/images/users/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void serveProfileImage(@PathVariable(name = "imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getProfileImage(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());

    }


}
