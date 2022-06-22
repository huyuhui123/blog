package com.z2011.blogapi.Service.Impl;

import com.z2011.blogapi.Service.UploadService;
import com.z2011.blogapi.dao.Result;
import com.z2011.blogapi.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private QiniuUtils qiniuUtils;
    @Override
    public Result upload(MultipartFile file) {
//        file.getName()和getOriginalFilename()有什么区别？
        String uploadName=file.getOriginalFilename();
//        System.out.println(file.getName() + "---" + file.getOriginalFilename());image---时间.png
//      StringUtils.substringAfterLast(originalFilename, ".");这和我用的不一样
        String fileName = UUID.randomUUID()+"."+uploadName.substring(uploadName.lastIndexOf(".") + 1, uploadName.length());
//      QiniuUtils是以bean的形式导入而不是直接new的好处我知道，可不可行呢？不可行，因为它用到了读取配置文件注解，必须以bean的形式注入，所以要以这个方式读取
//      七牛在上传时如果有问题会报错所以直接执行就可以，但它是有一个boolean返回值的，这一点记住要判断，不然没有上传失败的反映
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.SUCCESS(QiniuUtils.url+fileName);
        }
        return Result.FAIL(2001,"上传失败");
    }
}
