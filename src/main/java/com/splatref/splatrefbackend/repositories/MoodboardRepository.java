package com.splatref.splatrefbackend.repositories;

import com.splatref.splatrefbackend.entities.Image;
import com.splatref.splatrefbackend.entities.Moodboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoodboardRepository extends JpaRepository<Moodboard, Integer> {
}
