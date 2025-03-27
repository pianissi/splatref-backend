package com.splatref.splatrefbackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.splatref.splatrefbackend.auth.entities.User;
import com.splatref.splatrefbackend.dto.ImageDto;
import com.splatref.splatrefbackend.dto.MoodboardDto;
import com.splatref.splatrefbackend.dto.MoodboardMiniDto;
import com.splatref.splatrefbackend.service.MoodboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/moodboard")
@CrossOrigin(origins = "*")
public class MoodboardController {
    private final MoodboardService moodboardService;

    public MoodboardController(MoodboardService moodboardService) {
        this.moodboardService = moodboardService;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @PostMapping("/add-moodboard")
    public ResponseEntity<MoodboardDto> addMoodboardHandler(@RequestPart String moodboardDto) throws IOException {
        User user = getCurrentUser();

        return new ResponseEntity<>(moodboardService.addMoodboard(convertToMoodboardDto(moodboardDto), user.getUsername()), HttpStatus.CREATED);
    }

    public static MoodboardDto convertToMoodboardDto(String moodboardDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(moodboardDtoObj, MoodboardDto.class);
    }

    @GetMapping("/{moodboardId}")
    public ResponseEntity<MoodboardDto> getMoodboardHandler(@PathVariable Integer moodboardId) {
        User user = getCurrentUser();
        return ResponseEntity.ok(moodboardService.getMoodboard(moodboardId, user.getUsername()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MoodboardMiniDto>> getUserMoodboardHandler(Authentication authentication) {
//        User user = getCurrentUser();
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(moodboardService.getUserMoodboards(user.getUsername()));
    }

    @PutMapping("/update-moodboard/{moodboardId}")
    public ResponseEntity<MoodboardDto> updateMoodboardHandler(@PathVariable Integer moodboardId,
                                                               @RequestPart String moodboardDto) throws IOException {
        User user = getCurrentUser();

        return ResponseEntity.ok(moodboardService.updateMoodboard(moodboardId, convertToMoodboardDto(moodboardDto), user.getUsername()));
    }

    @DeleteMapping("/delete/{moodboardId}")
    public ResponseEntity<String> deleteMoodboardHandler(@PathVariable Integer moodboardId) throws IOException {
        User user = getCurrentUser();

        return ResponseEntity.ok(moodboardService.deleteMoodboard(moodboardId, user.getUsername()));
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteMoodboardHandler() throws IOException {
        User user = getCurrentUser();

        return ResponseEntity.ok(moodboardService.deleteUserMoodboards(user.getUsername()));
    }
}
