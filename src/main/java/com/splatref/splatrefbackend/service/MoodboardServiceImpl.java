package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.auth.repositories.UserRepository;
import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.dto.MoodboardDto;
import com.splatref.splatrefbackend.entities.Moodboard;
import com.splatref.splatrefbackend.exceptions.ImageNotFoundException;
import com.splatref.splatrefbackend.repositories.ImageRepository;
import com.splatref.splatrefbackend.repositories.MoodboardRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MoodboardServiceImpl implements MoodboardService {
    private final MoodboardRepository moodboardRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    private final ImageService imageService;

    public MoodboardServiceImpl(MoodboardRepository moodboardRepository, ImageRepository imageRepository, UserRepository userRepository, ImageService imageService) {
        this.moodboardRepository = moodboardRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
//    @PreAuthorize("#username == authentication.principal.username")
    public MoodboardDto addMoodboard(MoodboardDto moodboardDto, ImageDto thumbnailDto, MultipartFile thumbnail, String username) throws IOException {
        ImageDto imageDto = imageService.addImage(thumbnailDto, thumbnail);

        Moodboard moodboard = new Moodboard(
                null,
                moodboardDto.getName(),
                imageRepository.findByImageHash(imageDto.getImageHash()).orElseThrow(() -> new ImageNotFoundException("Thumbnail not found with hash: " + moodboardDto.getThumbnailHash())),
                moodboardDto.getData(),
                userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username))
        );

        Moodboard savedMoodboard = moodboardRepository.save(moodboard);
        // 6 map image object to dto
        MoodboardDto response = new MoodboardDto(
                savedMoodboard.getMoodboardId(),
                savedMoodboard.getName(),
                savedMoodboard.getThumbnail().getImageHash(),
                savedMoodboard.getData(),
                savedMoodboard.getOwner().getUserId()
        );

        return response;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public MoodboardDto getMoodboard(Integer moodboardId, String username) {
        Moodboard moodboard = moodboardRepository.findById(moodboardId).orElseThrow(() -> new RuntimeException("Moodboard not found with id: " + moodboardId));
        if (!userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(moodboard.getOwner().getUserId())) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }

        MoodboardDto response = new MoodboardDto(
                moodboard.getMoodboardId(),
                moodboard.getName(),
                moodboard.getThumbnail().getImageHash(),
                moodboard.getData(),
                moodboard.getOwner().getUserId()
        );
        return response;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public MoodboardDto updateMoodboard(Integer moodboardId, MoodboardDto moodboardDto, ImageDto thumbnailDto, MultipartFile thumbnail, String username) throws IOException {
        Moodboard oldMoodboard = moodboardRepository.findById(moodboardId).orElseThrow(() -> new RuntimeException("Moodboard not found with id: " + moodboardId));
        if (userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(oldMoodboard.getOwner().getUserId())) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }

        if (thumbnail != null) {
            imageService.deleteImage(oldMoodboard.getThumbnail().getImageId());
            ImageDto imageDto = imageService.addImage(thumbnailDto, thumbnail);
            thumbnailDto.setImageHash(imageDto.getImageHash());
        }

        Moodboard moodboard = new Moodboard(
                oldMoodboard.getMoodboardId(),
                moodboardDto.getName(),
                imageRepository.findByImageHash(thumbnailDto.getImageHash()).orElseThrow(() -> new ImageNotFoundException("Thumbnail not found with hash: " + thumbnailDto.getImageHash())),
                moodboardDto.getData(),
                userRepository.findById(moodboardDto.getOwnerId()).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + moodboardDto.getOwnerId()))
        );

        Moodboard savedMoodboard = moodboardRepository.save(moodboard);

        MoodboardDto response = new MoodboardDto(
                savedMoodboard.getMoodboardId(),
                savedMoodboard.getName(),
                savedMoodboard.getThumbnail().getImageHash(),
                savedMoodboard.getData(),
                savedMoodboard.getOwner().getUserId()
        );

        return response;
    }

    @Override
//    @PreAuthorize("#username == authentication.principal.username")
    public List<MoodboardDto> getUserMoodboards(String username) {
        List<Moodboard> moodboards = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getMoodboards();

        List<MoodboardDto> moodboardDtos = new ArrayList<>();

        for(Moodboard moodboard: moodboards) {
            MoodboardDto moodboardDto = new MoodboardDto(
                    moodboard.getMoodboardId(),
                    moodboard.getName(),
                    moodboard.getThumbnail().getImageHash(),
                    moodboard.getData(),
                    moodboard.getOwner().getUserId()
            );
            moodboardDtos.add(moodboardDto);
        }

        return moodboardDtos;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public String deleteMoodboard(Integer moodboardId, String username) throws IOException {
        Moodboard moodboard = moodboardRepository.findById(moodboardId).orElseThrow(() -> new RuntimeException("Moodboard not found with id: " + moodboardId));
        if (!userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(moodboard.getOwner().getUserId())) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }

        imageService.deleteImage(moodboard.getThumbnail().getImageId());

        // TODO, delete images in moodboard

        moodboardRepository.delete(moodboard);

        return "Moodboard deleted with id: " + moodboard.getMoodboardId();
    }
}
