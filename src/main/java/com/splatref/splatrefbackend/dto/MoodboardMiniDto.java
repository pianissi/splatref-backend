package com.splatref.splatrefbackend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.splatref.splatrefbackend.entities.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodboardMiniDto {
    private Integer moodboardId;
    private String name;
    private JsonNode thumbnail;
    private Integer ownerId;
}
