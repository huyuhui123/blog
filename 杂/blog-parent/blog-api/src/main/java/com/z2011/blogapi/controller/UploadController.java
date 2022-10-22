package com.z2011.blogapi.controller;

import com.z2011.blogapi.Service.UploadService;
import com.z2011.blogapi.dao.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * UUID的使用
 */
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file){
        return uploadService.upload(file);
    }
}
