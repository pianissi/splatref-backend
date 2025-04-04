package com.splatref.splatrefbackend.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.splatref.splatrefbackend.entities.Image;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoodboardDto {
    private Integer moodboardId;
    private String name;
    private JsonNode thumbnail;

    private String data;
//    private String thumbnailHash;
//    private JsonNode data;
    private Integer ownerId;
}
