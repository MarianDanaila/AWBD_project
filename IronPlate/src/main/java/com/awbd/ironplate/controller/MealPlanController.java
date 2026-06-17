package com.awbd.ironplate.controller;

import com.awbd.ironplate.domain.MealPlan;
import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.service.MealPlanService;
import com.awbd.ironplate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;
    private final UserService userService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @AuthenticationPrincipal UserDetails principal,
                       Model model) {
        User currentUser = userService.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        boolean isAdmin = principal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<MealPlan> mealPlans = isAdmin
                ? mealPlanService.findAll(pageable)
                : mealPlanService.findByUserId(currentUser.getId(), pageable);

        model.addAttribute("mealPlans", mealPlans);
        model.addAttribute("sort", sort);
        model.addAttribute("isAdmin", isAdmin);
        return "meal-plans/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("mealPlan", mealPlanService.findById(id));
        return "meal-plans/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("mealPlan", new MealPlan());
        return "meal-plans/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("mealPlan") MealPlan mealPlan,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails principal,
                         Model model) {
        if (result.hasErrors()) {
            return "meal-plans/form";
        }
        User currentUser = userService.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        mealPlan.setUser(currentUser);
        mealPlanService.save(mealPlan);
        return "redirect:/meal-plans";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("mealPlan", mealPlanService.findById(id));
        return "meal-plans/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("mealPlan") MealPlan mealPlan,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "meal-plans/form";
        }
        MealPlan existing = mealPlanService.findById(id);
        mealPlan.setId(id);
        mealPlan.setUser(existing.getUser());
        mealPlanService.save(mealPlan);
        return "redirect:/meal-plans";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        mealPlanService.deleteById(id);
        return "redirect:/meal-plans";
    }
}
