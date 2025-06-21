package com.intelijake.mall.controller;

import com.intelijake.mall.util.AWSUtil;
import com.intelijake.mall.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private AWSUtil AWSUtil;

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

        try {
            // 使用AWS S3上传文件
            String url = AWSUtil.uploadFile(newFileName, file.getInputStream(), file.getSize());
            return Result.ok("successfully uploaded", url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Upload failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String string = UUID.randomUUID().toString();
        System.out.println(string);//94cce23d-8d2b-4f4e-96d8-0fe86b296e6c
        System.out.println(string.replace("-", "")); //94cce23d8d2b4f4e96d80fe86b296e6c
    }
}