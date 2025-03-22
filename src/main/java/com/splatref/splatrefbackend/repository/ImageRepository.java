package com.splatref.splatrefbackend.repository;

import com.splatref.splatrefbackend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
