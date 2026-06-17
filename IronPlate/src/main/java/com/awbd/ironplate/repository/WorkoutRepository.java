package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    List<Workout> findByTrainingProgramId(Long trainingProgramId);

    List<Workout> findByTrainingProgramIdOrderByScheduledDateAsc(Long trainingProgramId);
}
