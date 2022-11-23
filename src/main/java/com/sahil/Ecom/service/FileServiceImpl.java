package com.sahil.Ecom.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(Long id,String path, MultipartFile file){
        //file name

        String name = file.getOriginalFilename();

 /*       String randomId = UUID.randomUUID().toString();

        String fileName = randomId.concat(name.substring(name.lastIndexOf(".")));*/

        String extention = name.substring(name.lastIndexOf("."));
        String fileName = id +extention;
        //full path

        String filePath = path + File.separator + fileName;

        ///create folder if not exist

        File f = new File(path);

        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            Files.copy(file.getInputStream(), Paths.get(filePath));
        }catch (Exception e){
            e.printStackTrace();
        }


        return fileName;

        //file copy
    }
}
