package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.TrainingProgram;
import com.awbd.ironplate.domain.TrainingProgram.DifficultyLevel;
import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.TrainingProgramRepository;
import com.awbd.ironplate.service.impl.TrainingProgramServiceImpl;
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
class TrainingProgramServiceImplTest {

    @Mock
    private TrainingProgramRepository trainingProgramRepository;

    @InjectMocks
    private TrainingProgramServiceImpl trainingProgramService;

    private TrainingProgram program;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("athlete");
        user.setRole(User.Role.ATHLETE);

        program = new TrainingProgram();
        program.setId(1L);
        program.setName("Beginner Full Body");
        program.setDifficultyLevel(DifficultyLevel.BEGINNER);
        program.setDurationWeeks(8);
        program.setUser(user);
    }

    @Test
    void save_shouldReturnSavedProgram() {
        when(trainingProgramRepository.save(program)).thenReturn(program);

        TrainingProgram result = trainingProgramService.save(program);

        assertThat(result.getName()).isEqualTo("Beginner Full Body");
        verify(trainingProgramRepository).save(program);
    }

    @Test
    void findById_shouldReturnProgram_whenExists() {
        when(trainingProgramRepository.findById(1L)).thenReturn(Optional.of(program));

        TrainingProgram result = trainingProgramService.findById(1L);

        assertThat(result.getDifficultyLevel()).isEqualTo(DifficultyLevel.BEGINNER);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(trainingProgramRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> trainingProgramService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingProgram> page = new PageImpl<>(List.of(program));
        when(trainingProgramRepository.findAll(pageable)).thenReturn(page);

        Page<TrainingProgram> result = trainingProgramService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByUserId_shouldReturnUserPrograms() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TrainingProgram> page = new PageImpl<>(List.of(program));
        when(trainingProgramRepository.findByUserId(1L, pageable)).thenReturn(page);

        Page<TrainingProgram> result = trainingProgramService.findByUserId(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getUsername()).isEqualTo("athlete");
    }

    @Test
    void findByDifficultyLevel_shouldReturnMatchingPrograms() {
        when(trainingProgramRepository.findByDifficultyLevel(DifficultyLevel.BEGINNER))
                .thenReturn(List.of(program));

        List<TrainingProgram> result = trainingProgramService.findByDifficultyLevel(DifficultyLevel.BEGINNER);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDifficultyLevel()).isEqualTo(DifficultyLevel.BEGINNER);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(trainingProgramRepository.existsById(1L)).thenReturn(true);

        trainingProgramService.deleteById(1L);

        verify(trainingProgramRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(trainingProgramRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> trainingProgramService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(trainingProgramRepository, never()).deleteById(any());
    }
}
