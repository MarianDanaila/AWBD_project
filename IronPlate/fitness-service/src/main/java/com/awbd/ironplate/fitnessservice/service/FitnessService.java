package com.awbd.ironplate.fitnessservice.service;

import com.awbd.ironplate.fitnessservice.client.UserClient;
import com.awbd.ironplate.fitnessservice.client.UserDto;
import com.awbd.ironplate.fitnessservice.domain.Exercise;
import com.awbd.ironplate.fitnessservice.domain.TrainingProgram;
import com.awbd.ironplate.fitnessservice.repository.ExerciseRepository;
import com.awbd.ironplate.fitnessservice.repository.TrainingProgramRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FitnessService {

    private final ExerciseRepository exerciseRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final UserClient userClient;

    public Page<Exercise> findAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable);
    }

    public Exercise findExerciseById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found: " + id));
    }

    public Exercise saveExercise(Exercise exercise) {
        log.info("Saving exercise: {}", exercise.getName());
        return exerciseRepository.save(exercise);
    }

    public void deleteExercise(Long id) {
        log.info("Deleting exercise: {}", id);
        exerciseRepository.deleteById(id);
    }

    public Page<TrainingProgram> findAllPrograms(Pageable pageable) {
        return trainingProgramRepository.findAll(pageable);
    }

    public Page<TrainingProgram> findProgramsByUser(Long userId, Pageable pageable) {
        return trainingProgramRepository.findByUserId(userId, pageable);
    }

    public TrainingProgram findProgramById(Long id) {
        return trainingProgramRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TrainingProgram not found: " + id));
    }

    @Retry(name = "user-service")
    public TrainingProgram saveProgram(TrainingProgram program) {
        if (program.getUserId() != null) {
            UserDto user = userClient.getUserById(program.getUserId());
            if (user == null) {
                log.warn("User {} not found or user-service unavailable — saving program anyway", program.getUserId());
            } else {
                log.info("Validated user {} for training program", user.getUsername());
            }
        }
        log.info("Saving training program: {}", program.getName());
        return trainingProgramRepository.save(program);
    }

    public void deleteProgram(Long id) {
        log.info("Deleting training program: {}", id);
        trainingProgramRepository.deleteById(id);
    }
}

