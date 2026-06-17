package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.domain.FoodItem.FoodCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodItemService {

    FoodItem save(FoodItem foodItem);

    FoodItem findById(Long id);

    Page<FoodItem> findAll(Pageable pageable);

    Page<FoodItem> findByName(String name, Pageable pageable);

    List<FoodItem> findByCategory(FoodCategory category);

    void deleteById(Long id);
}
