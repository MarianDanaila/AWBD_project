package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.TrainingProgram;
import com.awbd.ironplate.domain.TrainingProgram.DifficultyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {

    Page<TrainingProgram> findByUserId(Long userId, Pageable pageable);

    List<TrainingProgram> findByDifficultyLevel(DifficultyLevel difficultyLevel);

    Page<TrainingProgram> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
