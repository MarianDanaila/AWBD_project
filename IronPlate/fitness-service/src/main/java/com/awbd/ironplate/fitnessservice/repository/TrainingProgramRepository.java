package com.awbd.ironplate.fitnessservice.repository;

import com.awbd.ironplate.fitnessservice.domain.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    Page<TrainingProgram> findByUserId(Long userId, Pageable pageable);
}
