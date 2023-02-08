package com.sahil.ecom.controller;


import com.sahil.ecom.repository.ProductVariationRepository;
import com.sahil.ecom.service.impl.EmailSenderService;
import com.sahil.ecom.service.FileService;
import com.sahil.ecom.service.impl.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;


@RestController
public class testcontroller {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    FileService fileService;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    TemplateService templateService;

    @Value("${project.image}")
    private String path;

//    @GetMapping("/welcome")
//    public String  welcome(){
//        emailSenderService.sendEmail("sharma.sahil1560@gmail.com","TEST","TEST");
//
//        return "Lets go";
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> upload(@RequestParam("image")MultipartFile image){
//        ResponseDTO responseDTO = new ResponseDTO();
//        responseDTO.setTimestamp(new Date());
//
//        try{
//            String fileName = fileService.uploadImage(1L,path,image);
//            responseDTO.setResponseStatusCode(200);
//            responseDTO.setMessage("File Path : "+fileName);
//
//            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
//
//        }catch (Exception e){
//            responseDTO.setResponseStatusCode(400);
//            responseDTO.setMessage(null);
//
//            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
//        }
//
//
//
//
//
//
//    }


    @GetMapping("/products")
    public ResponseEntity<?> getProductsVars(){
        return ResponseEntity.ok(productVariationRepository.findAll());
    }


    @GetMapping("/template")
    public ResponseEntity<?> sendTempplateEmail() throws MessagingException {
        templateService.sendEmailTemplate();
        return ResponseEntity.ok(HttpStatus.OK);
    }



}
