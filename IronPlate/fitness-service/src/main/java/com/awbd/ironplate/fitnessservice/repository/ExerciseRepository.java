package com.awbd.ironplate.fitnessservice.repository;

import com.awbd.ironplate.fitnessservice.domain.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
