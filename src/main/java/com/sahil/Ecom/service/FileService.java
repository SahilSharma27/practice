package com.sahil.Ecom.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.InputStream;


public interface FileService {

    String uploadImage(Long id,String path, MultipartFile file);

    InputStream getImage(String path,String filename);


}
