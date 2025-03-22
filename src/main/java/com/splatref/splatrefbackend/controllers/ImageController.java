package com.splatref.splatrefbackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/add-image")
    public ResponseEntity<ImageDto> addImageHandler(@RequestPart MultipartFile file,
                                                    @RequestPart String imageDto) throws IOException {
        ImageDto dto = convertToImageDto(imageDto);
        return new ResponseEntity<>(imageService.addImage(dto, file), HttpStatus.CREATED);
    }

    private ImageDto convertToImageDto(String imageDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(imageDtoObj, ImageDto.class);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageDto> getImageHandler(@PathVariable Integer imageId) {
        return ResponseEntity.ok(imageService.getImage(imageId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ImageDto>> getAllImagesHandler() {
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @DeleteMapping("/delete/{imageId}")
    public ResponseEntity<String> deleteImageHandler(@PathVariable Integer imageId) throws IOException {
        return ResponseEntity.ok(imageService.deleteImage(imageId));
    }
}
