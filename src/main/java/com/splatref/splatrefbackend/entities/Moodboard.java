package com.splatref.splatrefbackend.entities;

import com.fasterxml.jackson.databind.JsonNode;
import com.splatref.splatrefbackend.auth.entities.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Moodboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer moodboardId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image thumbnail;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private JsonNode data;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}
