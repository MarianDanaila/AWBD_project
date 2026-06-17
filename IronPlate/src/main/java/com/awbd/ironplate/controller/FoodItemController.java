package com.awbd.ironplate.controller;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.service.FoodItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/food-items")
@RequiredArgsConstructor
public class FoodItemController {

    private final FoodItemService foodItemService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @RequestParam(required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<FoodItem> foodItems = (search != null && !search.isBlank())
                ? foodItemService.findByName(search, pageable)
                : foodItemService.findAll(pageable);

        model.addAttribute("foodItems", foodItems);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("categories", FoodItem.FoodCategory.values());
        return "food-items/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("foodItem", foodItemService.findById(id));
        return "food-items/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("foodItem", new FoodItem());
        model.addAttribute("categories", FoodItem.FoodCategory.values());
        return "food-items/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute FoodItem foodItem, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", FoodItem.FoodCategory.values());
            return "food-items/form";
        }
        foodItemService.save(foodItem);
        return "redirect:/food-items";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("foodItem", foodItemService.findById(id));
        model.addAttribute("categories", FoodItem.FoodCategory.values());
        return "food-items/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute FoodItem foodItem,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", FoodItem.FoodCategory.values());
            return "food-items/form";
        }
        foodItem.setId(id);
        foodItemService.save(foodItem);
        return "redirect:/food-items";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        foodItemService.deleteById(id);
        return "redirect:/food-items";
    }
}
