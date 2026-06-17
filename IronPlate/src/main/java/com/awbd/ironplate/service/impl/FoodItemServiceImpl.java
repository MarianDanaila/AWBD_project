package com.awbd.ironplate.service.impl;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.domain.FoodItem.FoodCategory;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.FoodItemRepository;
import com.awbd.ironplate.service.FoodItemService;
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
public class FoodItemServiceImpl implements FoodItemService {

    private final FoodItemRepository foodItemRepository;

    @Override
    @Transactional
    public FoodItem save(FoodItem foodItem) {
        log.debug("Saving food item: {}", foodItem.getName());
        return foodItemRepository.save(foodItem);
    }

    @Override
    @Transactional(readOnly = true)
    public FoodItem findById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FoodItem", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodItem> findAll(Pageable pageable) {
        return foodItemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FoodItem> findByName(String name, Pageable pageable) {
        return foodItemRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FoodItem> findByCategory(FoodCategory category) {
        return foodItemRepository.findByCategory(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting food item with id: {}", id);
        if (!foodItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("FoodItem", id);
        }
        foodItemRepository.deleteById(id);
    }
}
