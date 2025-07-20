package com.spring.batch.s3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${spring.s3.bucket}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws IOException {
       PutObjectRequest putObjectRequest = PutObjectRequest.builder()
               .bucket(bucketName)
               .key(file.getOriginalFilename())
               .contentType(file.getContentType())
               .build();

       s3Client.putObject(putObjectRequest,RequestBody.fromInputStream(file.getInputStream(),file.getSize()));
       return "File upload: "+file.getOriginalFilename();
    }

    public String downloadFile(String fileName) throws IOException {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        // File content as byte array
        byte[] bytes = s3Client.getObjectAsBytes(getRequest).asByteArray();

        // Path to save file in resources/downloads/
        String resourcePath = new File("src/main/resources/downloads").getAbsolutePath();
        File downloadDir = new File(resourcePath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        File outputFile = new File(downloadDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(bytes);
        }
        return "Downloaded and saved to: " + outputFile.getAbsolutePath();
    }

    public String deleteFile(String fileName) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteRequest);
        return "Deleted: " + fileName;
    }

    public List<String> listFiles() {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .build();
        return s3Client.listObjectsV2(listRequest).contents()
                .stream()
                .map(S3Object::key)
                .collect(Collectors.toList());
    }
}

