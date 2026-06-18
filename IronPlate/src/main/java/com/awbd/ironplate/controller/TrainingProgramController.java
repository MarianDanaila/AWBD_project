package com.awbd.ironplate.controller;

import com.awbd.ironplate.domain.Exercise;
import com.awbd.ironplate.domain.TrainingProgram;
import com.awbd.ironplate.domain.User;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.service.ExerciseService;
import com.awbd.ironplate.service.TrainingProgramService;
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

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/training-programs")
@RequiredArgsConstructor
public class TrainingProgramController {

    private final TrainingProgramService trainingProgramService;
    private final UserService userService;
    private final ExerciseService exerciseService;

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

        Page<TrainingProgram> programs = isAdmin
                ? trainingProgramService.findAll(pageable)
                : trainingProgramService.findByUserId(currentUser.getId(), pageable);

        model.addAttribute("programs", programs);
        model.addAttribute("sort", sort);
        model.addAttribute("isAdmin", isAdmin);
        return "training-programs/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("program", trainingProgramService.findById(id));
        return "training-programs/view";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("program", new TrainingProgram());
        model.addAttribute("difficultyLevels", TrainingProgram.DifficultyLevel.values());
        model.addAttribute("allExercises", exerciseService.findAll());
        return "training-programs/form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("program") TrainingProgram program,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails principal,
                         @RequestParam(required = false) List<Long> exerciseIds,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("difficultyLevels", TrainingProgram.DifficultyLevel.values());
            model.addAttribute("allExercises", exerciseService.findAll());
            return "training-programs/form";
        }
        User currentUser = userService.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        program.setUser(currentUser);
        if (exerciseIds != null) {
            List<Exercise> exercises = exerciseIds.stream()
                    .map(exerciseService::findById)
                    .collect(Collectors.toList());
            program.setExercises(exercises);
        }
        trainingProgramService.save(program);
        return "redirect:/training-programs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        TrainingProgram program = trainingProgramService.findById(id);
        model.addAttribute("program", program);
        model.addAttribute("difficultyLevels", TrainingProgram.DifficultyLevel.values());
        model.addAttribute("allExercises", exerciseService.findAll());
        model.addAttribute("selectedExerciseIds", program.getExercises().stream()
                .map(Exercise::getId)
                .collect(Collectors.toList()));
        return "training-programs/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("program") TrainingProgram program,
                         BindingResult result,
                         @AuthenticationPrincipal UserDetails principal,
                         @RequestParam(required = false) List<Long> exerciseIds,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("difficultyLevels", TrainingProgram.DifficultyLevel.values());
            model.addAttribute("allExercises", exerciseService.findAll());
            return "training-programs/form";
        }
        TrainingProgram existing = trainingProgramService.findById(id);
        program.setId(id);
        program.setUser(existing.getUser());
        List<Exercise> exercises = exerciseIds != null
                ? exerciseIds.stream().map(exerciseService::findById).collect(Collectors.toList())
                : List.of();
        program.setExercises(exercises);
        trainingProgramService.save(program);
        return "redirect:/training-programs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        trainingProgramService.deleteById(id);
        return "redirect:/training-programs";
    }
}
