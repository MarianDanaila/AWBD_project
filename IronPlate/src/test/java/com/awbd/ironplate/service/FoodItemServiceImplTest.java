package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.domain.FoodItem.FoodCategory;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.FoodItemRepository;
import com.awbd.ironplate.service.impl.FoodItemServiceImpl;
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
class FoodItemServiceImplTest {

    @Mock
    private FoodItemRepository foodItemRepository;

    @InjectMocks
    private FoodItemServiceImpl foodItemService;

    private FoodItem foodItem;

    @BeforeEach
    void setUp() {
        foodItem = new FoodItem();
        foodItem.setId(1L);
        foodItem.setName("Chicken Breast");
        foodItem.setCategory(FoodCategory.PROTEIN);
        foodItem.setCaloriesPer100g(165.0);
        foodItem.setProteinPer100g(31.0);
        foodItem.setCarbsPer100g(0.0);
        foodItem.setFatPer100g(3.6);
    }

    @Test
    void save_shouldReturnSavedFoodItem() {
        when(foodItemRepository.save(foodItem)).thenReturn(foodItem);

        FoodItem result = foodItemService.save(foodItem);

        assertThat(result.getName()).isEqualTo("Chicken Breast");
        verify(foodItemRepository).save(foodItem);
    }

    @Test
    void findById_shouldReturnFoodItem_whenExists() {
        when(foodItemRepository.findById(1L)).thenReturn(Optional.of(foodItem));

        FoodItem result = foodItemService.findById(1L);

        assertThat(result.getCategory()).isEqualTo(FoodCategory.PROTEIN);
    }

    @Test
    void findById_shouldThrow_whenNotFound() {
        when(foodItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> foodItemService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_shouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FoodItem> page = new PageImpl<>(List.of(foodItem));
        when(foodItemRepository.findAll(pageable)).thenReturn(page);

        Page<FoodItem> result = foodItemService.findAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByName_shouldReturnMatchingItems() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FoodItem> page = new PageImpl<>(List.of(foodItem));
        when(foodItemRepository.findByNameContainingIgnoreCase("chicken", pageable)).thenReturn(page);

        Page<FoodItem> result = foodItemService.findByName("chicken", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Chicken Breast");
    }

    @Test
    void findByCategory_shouldReturnItemsInCategory() {
        when(foodItemRepository.findByCategory(FoodCategory.PROTEIN)).thenReturn(List.of(foodItem));

        List<FoodItem> result = foodItemService.findByCategory(FoodCategory.PROTEIN);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(FoodCategory.PROTEIN);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(foodItemRepository.existsById(1L)).thenReturn(true);

        foodItemService.deleteById(1L);

        verify(foodItemRepository).deleteById(1L);
    }

    @Test
    void deleteById_shouldThrow_whenNotFound() {
        when(foodItemRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> foodItemService.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(foodItemRepository, never()).deleteById(any());
    }
}
