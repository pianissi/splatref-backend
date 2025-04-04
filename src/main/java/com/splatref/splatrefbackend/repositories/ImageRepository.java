package com.splatref.splatrefbackend.repositories;

import com.splatref.splatrefbackend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> findByImageHash(String imageHash);
}
