package com.splatref.splatrefbackend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(indexes = @Index(name = "hash_index", columnList = "imageHash", unique = true))
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(nullable = false)
    @NotBlank(message = "Image must have a Hash")
    private String imageHash;

    @NotBlank(message = "Image must have an Extension")
    private String imageExtension;

    private Integer referenceCount;
}
