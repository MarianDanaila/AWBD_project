package com.awbd.ironplate.service.impl;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.domain.Exercise.ExerciseType;
import com.awbd.ironplate.domain.Exercise.MuscleGroup;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.ExerciseRepository;
import com.awbd.ironplate.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    @Transactional
    public Exercise save(Exercise exercise) {
        log.debug("Saving exercise: {}", exercise.getName());
        Exercise saved = exerciseRepository.save(exercise);
        log.info("Exercise saved with id: {}", saved.getId());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Exercise not found with id: {}", id);
                    return new ResourceNotFoundException("Exercise", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findAll(Pageable pageable) {
        return exerciseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Exercise> findByName(String name, Pageable pageable) {
        return exerciseRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup) {
        return exerciseRepository.findByMuscleGroup(muscleGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Exercise> findByExerciseType(ExerciseType exerciseType) {
        return exerciseRepository.findByExerciseType(exerciseType);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting exercise with id: {}", id);
        if (!exerciseRepository.existsById(id)) {
            log.error("Cannot delete exercise - not found with id: {}", id);
            throw new ResourceNotFoundException("Exercise", id);
        }
        exerciseRepository.deleteById(id);
        log.info("Exercise deleted with id: {}", id);
    }
}
