package com.sahil.Ecom.controller;


import com.sahil.Ecom.dto.ResponseDTO;
import com.sahil.Ecom.service.EmailSenderService;
import com.sahil.Ecom.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;


@RestController
public class testcontroller {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    FileService fileService;

    @Value("${project.image}")
    private String path;

    @GetMapping("/welcome")
    public String  welcome(){
        emailSenderService.sendEmail("sharma.sahil1560@gmail.com","TEST","TEST");

        return "Lets go";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("image")MultipartFile image){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setTimestamp(new Date());

        try{
            String fileName = fileService.uploadImage(1L,path,image);
            responseDTO.setResponseStatusCode(200);
            responseDTO.setMessage("File Path : "+fileName);

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);

        }catch (Exception e){
            responseDTO.setResponseStatusCode(400);
            responseDTO.setMessage(null);

            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }






    }




}
