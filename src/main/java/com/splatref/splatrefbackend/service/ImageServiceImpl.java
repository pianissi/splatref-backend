package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.entities.Image;
import com.splatref.splatrefbackend.exceptions.FileExistsException;
import com.splatref.splatrefbackend.repositories.ImageRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Value("${project.images}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public ImageServiceImpl(ImageRepository imageRepository, FileService fileService) {
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    @Override
    public ImageDto addImage(ImageDto imageDto, MultipartFile file) throws IOException {
        // 1 upload file

        String uploadedFileName = fileService.uploadFile(path, file);

        if (Files.exists(Paths.get(path + File.separator + uploadedFileName))) {
            throw new FileExistsException("File already exists!");
        }
        // 2 set value of filename
        imageDto.setImageHash(FilenameUtils.getBaseName(uploadedFileName));
        imageDto.setImageExtension(FilenameUtils.getExtension(uploadedFileName));

        // 3 map dto to object
        Image image = new Image(
                null,
                imageDto.getImageHash(),
                imageDto.getImageExtension()
        );
        // 4 save object
        Image savedImage = imageRepository.save(image);
        // 5 generate image url
        String imageUrl =  baseUrl + "/file/" + uploadedFileName;
        // 6 map image object to dto
        ImageDto response = new ImageDto(
                savedImage.getImageId(),
                savedImage.getImageHash(),
                savedImage.getImageExtension(),
                imageUrl
        );

        return response;
    }

    @Override
    public ImageDto getImage(Integer imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        String imageUrl =  baseUrl + "/file/" + image.getImageHash() + "." + image.getImageExtension();

        ImageDto response = new ImageDto(
            image.getImageId(),
            image.getImageHash(),
            image.getImageExtension(),
            imageUrl
        );
        return response;
    }

    @Override
    public List<ImageDto> getAllImages() {
        List<Image> images = imageRepository.findAll();

        List<ImageDto> imageDtos = new ArrayList<>();

        for(Image image: images) {
            String imageUrl =  baseUrl + "/file/" + image.getImageHash() + "." + image.getImageExtension();
            ImageDto imageDto = new ImageDto(
                image.getImageId(),
                image.getImageHash(),
                image.getImageExtension(),
                imageUrl
            );
            imageDtos.add(imageDto);
        }

        return imageDtos;
    }

    @Override
    public String deleteImage(Integer imageId) throws IOException {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
        Integer id = image.getImageId();

        Files.deleteIfExists(Paths.get(path + File.separator + image.getImageHash() + "." + image.getImageExtension()));

        imageRepository.delete(image);

        return "Image deleted with id: " + id;
    }
}
