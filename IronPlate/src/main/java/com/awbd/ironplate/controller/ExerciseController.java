package com.awbd.ironplate.controller;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @RequestParam(required = false) String search,
                       Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Exercise> exercises = (search != null && !search.isBlank())
                ? exerciseService.findByName(search, pageable)
                : exerciseService.findAll(pageable);

        model.addAttribute("exercises", exercises);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("muscleGroups", Exercise.MuscleGroup.values());
        model.addAttribute("exerciseTypes", Exercise.ExerciseType.values());
        return "exercises/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("exercise", exerciseService.findById(id));
        return "exercises/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        model.addAttribute("muscleGroups", Exercise.MuscleGroup.values());
        model.addAttribute("exerciseTypes", Exercise.ExerciseType.values());
        return "exercises/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute Exercise exercise, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("muscleGroups", Exercise.MuscleGroup.values());
            model.addAttribute("exerciseTypes", Exercise.ExerciseType.values());
            return "exercises/form";
        }
        exerciseService.save(exercise);
        return "redirect:/exercises";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("exercise", exerciseService.findById(id));
        model.addAttribute("muscleGroups", Exercise.MuscleGroup.values());
        model.addAttribute("exerciseTypes", Exercise.ExerciseType.values());
        return "exercises/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute Exercise exercise,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("muscleGroups", Exercise.MuscleGroup.values());
            model.addAttribute("exerciseTypes", Exercise.ExerciseType.values());
            return "exercises/form";
        }
        exercise.setId(id);
        exerciseService.save(exercise);
        return "redirect:/exercises";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        exerciseService.deleteById(id);
        return "redirect:/exercises";
    }
}
