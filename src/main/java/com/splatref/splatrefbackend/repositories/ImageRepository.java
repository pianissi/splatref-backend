package com.splatref.splatrefbackend.repositories;

import com.splatref.splatrefbackend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}
