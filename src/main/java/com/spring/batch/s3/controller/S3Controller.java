package com.spring.batch.s3.controller;

import com.spring.batch.s3.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(s3Service.uploadFile(file));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<String> downloadAndSave(@PathVariable String fileName) throws IOException {
        String message = s3Service.downloadFile(fileName);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/delete/{fileName}")
    public ResponseEntity<String> delete(@PathVariable String fileName) {
        return ResponseEntity.ok(s3Service.deleteFile(fileName));
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        return ResponseEntity.ok(s3Service.listFiles());
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> list(){
        return ResponseEntity.ok(s3Service.listFiles());
    }
}
