package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    List<WorkoutExercise> findByWorkoutId(Long workoutId);

    List<WorkoutExercise> findByExerciseId(Long exerciseId);

    void deleteByWorkoutId(Long workoutId);
}
