package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.dto.MoodboardDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MoodboardService {
    public MoodboardDto addMoodboard(MoodboardDto moodboardDto, ImageDto thumbnailDto, MultipartFile thumbnail, String username) throws IOException;

    MoodboardDto getMoodboard(Integer moodboardId, String username);

    MoodboardDto updateMoodboard(Integer moodboardId, MoodboardDto moodboardDto, ImageDto thumbnailDto, MultipartFile thumbnail, String username) throws IOException;

    List<MoodboardDto> getUserMoodboards(String username);

    String deleteMoodboard(Integer moodboardId, String username) throws IOException;
}
