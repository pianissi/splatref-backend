package com.splatref.splatrefbackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Integer imageId;

    @NotBlank(message = "Image must have a Hash")
    private String imageHash;

    @NotBlank(message = "Image must have an Extension")
    private String imageExtension;

    @NotBlank(message = "Image must have a url or file location")
    private String imageUrl;
}
