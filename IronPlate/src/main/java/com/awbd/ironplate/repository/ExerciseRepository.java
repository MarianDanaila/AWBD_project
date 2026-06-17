package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.domain.Exercise.MuscleGroup;
import com.awbd.ironplate.domain.Exercise.ExerciseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Page<Exercise> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findByExerciseType(ExerciseType exerciseType);

    List<Exercise> findByMuscleGroupAndExerciseType(MuscleGroup muscleGroup, ExerciseType exerciseType);
}
