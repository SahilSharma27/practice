package com.sahil.Ecom.controller;

import com.sahil.Ecom.dto.password.ForgotPasswordDTO;
import com.sahil.Ecom.dto.LoginResponseDTO;
import com.sahil.Ecom.dto.password.ResetPassDTO;
import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.InvalidTokenException;
import com.sahil.Ecom.exception.PassConfirmPassNotMatchingException;
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
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Locale;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenGeneratorHelper tokenGeneratorHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MessageSource messageSource;

    @Value("${project.image}")
    private String path;


    @Value("${project.image.product}")
    private String pathProduct;


    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private LoginService loginService;

    @Autowired
    private BlacklistTokenRepository blacklistTokenRepository;

    @Autowired
    private FileService fileService;


    Locale locale = LocaleContextHolder.getLocale();

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

            logger.info("------------------REFRESH________________");
            logger.info(newAccessToken);

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

        if(userService.activateByEmail(email)) {
            String message = messageSource.getMessage("user.account.activated", null, "message", locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true, message, HttpStatus.OK));
        }

        ResponseDTO responseDTO = new ResponseDTO(LocalDateTime.now(), false, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDTO.setMessage(messageSource.getMessage("user.not.activated", null, "message", locale));

        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping(value = "/users/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {

//        1 check Email in db
//        2 generate url
//        3 send mail

        ResponseDTO responseDTO;
        String message;

        if(userService.forgotPasswordHelper(forgotPasswordDTO.getUserEmail())){
            message = messageSource.getMessage("reset.pass.email.sent",null,"message",locale);
            responseDTO= new ResponseDTO(LocalDateTime.now(),true,message,HttpStatus.OK);

            return ResponseEntity.ok(responseDTO);
        }

        message = messageSource.getMessage("email.not.sent",null,"message",locale);
        responseDTO = new ResponseDTO(LocalDateTime.now(),false,message,HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);


    }

    @PutMapping(value = "/users/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPassDTO resetPassDTO) {
//        1 check token in db
//        check time limit
//        2 get email
//        3 update account pass

        if (resetPassDTO.getNewPassword().equals(resetPassDTO.getConfirmNewPassword())) {
            String email = userService.validateResetPasswordToken(token);

            if (email.equals(resetPassDTO.getUserEmail())) {
                userService.resetPassword(email, resetPassDTO.getNewPassword());
            }
            String message = messageSource.getMessage("user.password.updated",null,"message",locale);
            return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true,message, HttpStatus.OK));
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

                accessToken = requestHeader.substring("Bearer ".length());
                try {
                    username = this.jwtUtil.extractUsername(accessToken);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new InvalidTokenException();
                }

            }
            if (userService.resetPassword(username, resetPassDTO.getNewPassword()))
                return ResponseEntity.ok(new ResponseDTO(LocalDateTime.now(), true,"User Password Updated", HttpStatus.OK));
        }

        throw new PassConfirmPassNotMatchingException();

    }

    @GetMapping(value = "/users/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization")String requestHeader) {

        String username = null;
        String accessToken = null;

        ResponseDTO responseDTO =  new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());


        //check format
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {

            accessToken = requestHeader.substring("Bearer".length());

            try {
                username=jwtUtil.extractUsername(accessToken);
            }catch (Exception e){
                e.printStackTrace();
            }

            if(userService.logoutHelper(username)){
                responseDTO.setSuccess(true);
                responseDTO.setResponseStatusCode(HttpStatus.OK);
                responseDTO.setMessage(messageSource.getMessage("user.logged.out",null,"message",locale));
                return ResponseEntity.ok(responseDTO);
            }

        }

        throw new InvalidTokenException();
    }

    //common for customer and seller
    @PatchMapping(value = "users/address/{address_id}")
    public ResponseEntity<?> updateAddress(@RequestBody Address address, @PathVariable(name = "address_id") Long id) {

        //find address by id
        //update given fields

        userService.updateAddress(id, address);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());
        String message;
        message = messageSource.getMessage("user.address.updated", null, "message", locale);
        responseDTO.setResponseStatusCode(HttpStatus.OK);
        responseDTO.setMessage(message);
        return ResponseEntity.ok(responseDTO);

    }

    @PostMapping(value = "users/image/{user_id}")
    public ResponseEntity<?> upload(@PathVariable(name = "user_id") Long id, @RequestParam("image") MultipartFile image) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(LocalDateTime.now());


        logger.info("--------------------------"+image.getContentType());

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
