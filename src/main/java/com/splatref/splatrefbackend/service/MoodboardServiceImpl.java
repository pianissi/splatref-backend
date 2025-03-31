package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.auth.repositories.UserRepository;
import com.splatref.splatrefbackend.dto.MoodboardDto;
import com.splatref.splatrefbackend.dto.MoodboardMiniDto;
import com.splatref.splatrefbackend.entities.Moodboard;
import com.splatref.splatrefbackend.repositories.ImageRepository;
import com.splatref.splatrefbackend.repositories.MoodboardRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    public MoodboardDto addMoodboard(MoodboardDto moodboardDto, String username) throws IOException {
        Moodboard moodboard = new Moodboard(
                null,
                moodboardDto.getName(),
                moodboardDto.getThumbnail(),
                moodboardDto.getData().getBytes(StandardCharsets.UTF_16),
                userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username))
        );

        Moodboard savedMoodboard = moodboardRepository.save(moodboard);

        MoodboardDto response = new MoodboardDto(
                savedMoodboard.getMoodboardId(),
                savedMoodboard.getName(),
                savedMoodboard.getThumbnail(),
                new String(savedMoodboard.getData(), StandardCharsets.UTF_16),
                savedMoodboard.getOwner().getUserId()
        );

        return response;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public MoodboardDto getMoodboard(Integer moodboardId, String username) {
        Moodboard moodboard = moodboardRepository.findById(moodboardId).orElseThrow(() -> new RuntimeException("Moodboard not found with id: " + moodboardId));
        if (!userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(moodboard.getOwner().getUserId())) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }

        MoodboardDto response = new MoodboardDto(
                moodboard.getMoodboardId(),
                moodboard.getName(),
                moodboard.getThumbnail(),
                new String(moodboard.getData(), StandardCharsets.UTF_16),
                moodboard.getOwner().getUserId()
        );
        return response;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public MoodboardDto updateMoodboard(Integer moodboardId, MoodboardDto moodboardDto, String username) throws IOException {
        Moodboard moodboard = moodboardRepository.findById(moodboardId).orElseThrow(() -> new RuntimeException("Moodboard not found with id: " + moodboardId));
        if (!(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(moodboard.getOwner().getUserId()))) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }

        moodboard.setData(moodboardDto.getData().getBytes(StandardCharsets.UTF_16));
        moodboard.setThumbnail(moodboardDto.getThumbnail());
        moodboard.setName(moodboardDto.getName());
        Moodboard savedMoodboard = moodboardRepository.save(moodboard);

        MoodboardDto response = new MoodboardDto(
                savedMoodboard.getMoodboardId(),
                savedMoodboard.getName(),
                savedMoodboard.getThumbnail(),
                new String(savedMoodboard.getData(), StandardCharsets.UTF_16),
                savedMoodboard.getOwner().getUserId()
        );

        return response;
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public List<MoodboardMiniDto> getUserMoodboards(String username) {
        List<Moodboard> moodboards = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getMoodboards();

        List<MoodboardMiniDto> moodboardDtos = new ArrayList<>();

        for(Moodboard moodboard: moodboards) {
            MoodboardMiniDto moodboardDto = new MoodboardMiniDto(
                    moodboard.getMoodboardId(),
                    moodboard.getName(),
                    moodboard.getThumbnail(),
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
        if (!userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getUserId().equals(moodboard.getOwner().getUserId())) {
            throw new AccessDeniedException("Username: " + username + ", does not own moodboard with id: " + moodboardId);
        }


        moodboardRepository.delete(moodboard);

        return "Moodboard deleted with id: " + moodboard.getMoodboardId();
    }

    @Override
    @PreAuthorize("#username == authentication.principal.username")
    public String deleteUserMoodboards(String username) throws IOException {
        List<Moodboard> moodboards = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)).getMoodboards();

        List<MoodboardDto> moodboardDtos = new ArrayList<>();

        for(Moodboard moodboard: moodboards) {
            moodboardRepository.delete(moodboard);
        }
        return "Moodboards for user: " + username + ",has been deleted";
    }
}
