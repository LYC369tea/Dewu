package com.lyc.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.lyc.config.OSSConfig;
import com.lyc.service.OSSService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
public class OSSServiceImpl implements OSSService {
    @Autowired
    private OSSConfig ossConfig;

    public String uploadImage(MultipartFile file, String targetPath){
        String originalFilename = file.getOriginalFilename();
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String randomFileName = generateRandomFileName(fileExtension);

        String objectKey = normalizeObjectKey(targetPath, randomFileName);

        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

        try {
            ossClient.putObject(ossConfig.getBucketName(), objectKey, file.getInputStream());
            Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);

            return ossClient.generatePresignedUrl(ossConfig.getBucketName(), objectKey, expiration).toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public void deleteImage(String imageUrl){
        String objectKey = extractObjectKeyFromUrl(imageUrl);
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), objectKey);
        } finally {
            ossClient.shutdown();
        }
    }

    private String extractObjectKeyFromUrl(String imageUrl) {
        // 从完整的URL中提取对象键
        String prefix = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/";
        return imageUrl.substring(prefix.length());
    }

    private String generateRandomFileName(String fileExtension) {
        String randomName = UUID.randomUUID().toString();
        return randomName + "." + fileExtension;
    }

    private String normalizeObjectKey(String targetPath, String fileName) {
        targetPath = targetPath.replaceAll("^/+", "");

        if (targetPath.isEmpty()) {
            return fileName;
        }

        if (!targetPath.endsWith("/")) {
            targetPath += "/";
        }

        return targetPath + fileName;
    }

    private String generateFileUrl(String objectKey) {
        return "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint().substring(7) + "/" + objectKey;
    }
}
