package com.splatref.splatrefbackend.controllers;

import com.splatref.splatrefbackend.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${project.images}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) throws IOException {
        String uploadedFileName;
        try {
            uploadedFileName = fileService.uploadFile(path, file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


        return ResponseEntity.ok("File uploaded : " + uploadedFileName);
    }

    @GetMapping("/{fileName}")
    public void serveFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException{
        String fileExtension = FilenameUtils.getExtension(fileName);
        InputStream resourceFile = fileService.getResourceFile(path, fileName);;
        if (fileExtension.equals("png")) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        } else if (fileExtension.equals("jpg")) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        } else {
            throw new IllegalArgumentException("Served file was not an image!");
        }

        StreamUtils.copy(resourceFile, response.getOutputStream());
    }
}
