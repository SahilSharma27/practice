package com.sahil.ecom.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;


public interface FileService {

    String uploadImage(Long id,String path, MultipartFile file);

    InputStream getImage(String path,String filename);


}
