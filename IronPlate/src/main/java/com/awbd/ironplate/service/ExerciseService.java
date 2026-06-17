package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.domain.Exercise.ExerciseType;
import com.awbd.ironplate.domain.Exercise.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExerciseService {

    Exercise save(Exercise exercise);

    Exercise findById(Long id);

    Page<Exercise> findAll(Pageable pageable);

    Page<Exercise> findByName(String name, Pageable pageable);

    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);

    List<Exercise> findByExerciseType(ExerciseType exerciseType);

    void deleteById(Long id);
}
