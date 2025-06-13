package com.intelijake.mall.controller;

import com.intelijake.mall.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * ClassName: UploadController
 * Description:
 * <p>
 * Datetime: 2025/5/28 20:05
 * Author: @Likun.Fang
 * Version: 1.0
 */

@RestController
public class UploadController {

    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        if (file == null || file.isEmpty()) {
            return Result.error("File is empty");
        }

        String fileName = UUID.randomUUID().toString().replace("-", "");

        //assume avatar name is a.png
        String avatarName = file.getOriginalFilename();
        if (avatarName == null || avatarName.isEmpty()) {
            return Result.error("Invalid file name");
        }

        String suffix = avatarName.substring(avatarName.lastIndexOf("."));

        String newFileName = fileName + suffix;

        String projectRoot = System.getProperty("user.dir");

        // Create upload directory if it doesn't exist
        String uploadDir = projectRoot + "/userpics/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = uploadDir + newFileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok("successfully uploaded",newFileName);
    }

    public static void main(String[] args) {
        String string = UUID.randomUUID().toString();
        System.out.println(string);//94cce23d-8d2b-4f4e-96d8-0fe86b296e6c
        System.out.println(string.replace("-", "")); //94cce23d8d2b4f4e96d80fe86b296e6c
    }
}