package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.TrainingProgram;
import com.awbd.ironplate.domain.TrainingProgram.DifficultyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrainingProgramService {

    TrainingProgram save(TrainingProgram trainingProgram);

    TrainingProgram findById(Long id);

    Page<TrainingProgram> findAll(Pageable pageable);

    Page<TrainingProgram> findByUserId(Long userId, Pageable pageable);

    List<TrainingProgram> findByDifficultyLevel(DifficultyLevel difficultyLevel);

    void deleteById(Long id);
}
