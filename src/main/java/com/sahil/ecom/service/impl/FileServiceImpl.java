package com.sahil.ecom.service.impl;


import com.sahil.ecom.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(Long id,String path, MultipartFile file){
        //file name

        String name = file.getOriginalFilename();


        String extension = name.substring(name.lastIndexOf("."));
        String fileName = id + extension;
        //full path

        String filePath = path + File.separator + fileName;

        ///create folder if not exist

        File f = new File(path);

        if (!f.exists()) {
            f.mkdirs();
        }

//        if(f.exists() && !f.isDirectory()) {
//            // do something
//            return "localhost:8080/images/users/"+id+".jpg";
//        }

        try {
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e){
            e.printStackTrace();
        }


        return fileName;

        //file copy
    }

    @Override
    public InputStream getImage(String path, String filename) {
        //file separator is used in place of " / " as depending on the OS it may require " \ "

        String fullPath = path+File.separator+filename;
        InputStream inputStream;
        try {
             inputStream =  new FileInputStream(fullPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }


}
