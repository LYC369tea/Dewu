package com.lyc.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OSSService {

    String uploadImage(MultipartFile file, String targetPath);

    void deleteImage(String imageUrl);
}
