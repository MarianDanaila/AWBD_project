package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.MealPlan;
import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.MealPlanRepository;
import com.awbd.ironplate.service.impl.MealPlanServiceImpl;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealPlanServiceImplTest {

    @Mock
    private MealPlanRepository mealPlanRepository;

    @InjectMocks
    private MealPlanServiceImpl mealPlanService;

    private MealPlan mealPlan;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("athlete");
        user.setRole(User.Role.ATHLETE);

        mealPlan = new MealPlan();
        mealPlan.setId(1L);
        mealPlan.setName("Cutting Plan");
        mealPlan.setDailyCalorieTarget(2000);
        mealPlan.setStartDate(LocalDate.of(2026, 1, 1));
        mealPlan.setEndDate(LocalDate.of(2026, 3, 31));
        mealPlan.setUser(user);
    }

    @Test
    void save_shouldReturnSavedMealPlan() {
        when(mealPlanRepository.save(mealPlan)).thenReturn(mealPlan);

        MealPlan result = mealPlanService.save(mealPlan);

        assertThat(result.getName()).isEqualTo("Cutting Plan");
        verify(mealPlanRepository).save(mealPlan);
    }

    @Test
    void findById_shouldReturnMealPlan_whenExists() {
        when(mealPlanRepository.findById(1L)).thenReturn(Optional.of(mealPlan));

        MealPlan result = mealPlanService.findById(1L);

        assertThat(result.getDailyCalorieTarget()).isEqualTo(2000);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(mealPlanRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> mealPlanService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MealPlan> page = new PageImpl<>(List.of(mealPlan));
        when(mealPlanRepository.findAll(pageable)).thenReturn(page);

        Page<MealPlan> result = mealPlanService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByUserId_shouldReturnUserMealPlans() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MealPlan> page = new PageImpl<>(List.of(mealPlan));
        when(mealPlanRepository.findByUserId(1L, pageable)).thenReturn(page);

        Page<MealPlan> result = mealPlanService.findByUserId(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getUser().getUsername()).isEqualTo("athlete");
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(mealPlanRepository.existsById(1L)).thenReturn(true);

        mealPlanService.deleteById(1L);

        verify(mealPlanRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(mealPlanRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> mealPlanService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(mealPlanRepository, never()).deleteById(any());
    }
}
