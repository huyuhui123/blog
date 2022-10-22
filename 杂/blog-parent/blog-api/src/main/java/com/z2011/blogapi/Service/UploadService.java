package com.z2011.blogapi.Service;

import com.z2011.blogapi.dao.Result;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Result upload(MultipartFile file);
}
