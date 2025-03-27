package com.splatref.splatrefbackend.service;

import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.dto.MoodboardDto;
import com.splatref.splatrefbackend.dto.MoodboardMiniDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MoodboardService {
    //    @PreAuthorize("#username == authentication.principal.username")
    MoodboardDto addMoodboard(MoodboardDto moodboardDto, String username) throws IOException;

    MoodboardDto getMoodboard(Integer moodboardId, String username);

    MoodboardDto updateMoodboard(Integer moodboardId, MoodboardDto moodboardDto, String username) throws IOException;

    List<MoodboardMiniDto> getUserMoodboards(String username);

    String deleteMoodboard(Integer moodboardId, String username) throws IOException;

    String deleteUserMoodboards(String username) throws IOException;
}
