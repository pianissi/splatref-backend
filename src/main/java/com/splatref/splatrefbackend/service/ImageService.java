package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageDto addImage(ImageDto imageDto, MultipartFile file) throws IOException;

    ImageDto getImage(Integer imageId);

    List<ImageDto> getAllImages();

    String deleteImage(Integer imageId) throws IOException;
}
