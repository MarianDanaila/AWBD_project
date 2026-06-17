package com.awbd.ironplate.service.impl;

import com.awbd.ironplate.domain.MealPlan;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.MealPlanRepository;
import com.awbd.ironplate.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    @Override
    @Transactional
    public MealPlan save(MealPlan mealPlan) {
        log.debug("Saving meal plan: {}", mealPlan.getName());
        return mealPlanRepository.save(mealPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public MealPlan findById(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MealPlan", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MealPlan> findAll(Pageable pageable) {
        return mealPlanRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MealPlan> findByUserId(Long userId, Pageable pageable) {
        return mealPlanRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting meal plan with id: {}", id);
        if (!mealPlanRepository.existsById(id)) {
            throw new ResourceNotFoundException("MealPlan", id);
        }
        mealPlanRepository.deleteById(id);
    }
}
