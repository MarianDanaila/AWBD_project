package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.domain.Exercise.ExerciseType;
import com.awbd.ironplate.domain.Exercise.MuscleGroup;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.ExerciseRepository;
import com.awbd.ironplate.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    private Exercise exercise;

    @BeforeEach
    void setUp() {
        exercise = new Exercise();
        exercise.setId(1L);
        exercise.setName("Bench Press");
        exercise.setMuscleGroup(MuscleGroup.CHEST);
        exercise.setExerciseType(ExerciseType.STRENGTH);
    }

    @Test
    void save_shouldReturnSavedExercise() {
        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        Exercise result = exerciseService.save(exercise);

        assertThat(result.getName()).isEqualTo("Bench Press");
        verify(exerciseRepository).save(exercise);
    }

    @Test
    void findById_shouldReturnExercise_whenExists() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise result = exerciseService.findById(1L);

        assertThat(result.getMuscleGroup()).isEqualTo(MuscleGroup.CHEST);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(exerciseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> exerciseService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise));
        when(exerciseRepository.findAll(pageable)).thenReturn(page);

        Page<Exercise> result = exerciseService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByName_shouldReturnMatchingExercises() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Exercise> page = new PageImpl<>(List.of(exercise));
        when(exerciseRepository.findByNameContainingIgnoreCase("bench", pageable)).thenReturn(page);

        Page<Exercise> result = exerciseService.findByName("bench", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Bench Press");
    }

    @Test
    void findByMuscleGroup_shouldReturnMatchingExercises() {
        when(exerciseRepository.findByMuscleGroup(MuscleGroup.CHEST)).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.findByMuscleGroup(MuscleGroup.CHEST);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMuscleGroup()).isEqualTo(MuscleGroup.CHEST);
    }

    @Test
    void findByExerciseType_shouldReturnMatchingExercises() {
        when(exerciseRepository.findByExerciseType(ExerciseType.STRENGTH)).thenReturn(List.of(exercise));

        List<Exercise> result = exerciseService.findByExerciseType(ExerciseType.STRENGTH);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExerciseType()).isEqualTo(ExerciseType.STRENGTH);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(exerciseRepository.existsById(1L)).thenReturn(true);

        exerciseService.deleteById(1L);

        verify(exerciseRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(exerciseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> exerciseService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(exerciseRepository, never()).deleteById(any());
    }
}
