package com.awbd.ironplate.fitnessservice.controller;

import com.awbd.ironplate.fitnessservice.domain.Exercise;
import com.awbd.ironplate.fitnessservice.domain.TrainingProgram;
import com.awbd.ironplate.fitnessservice.service.FitnessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FitnessController {

    private final FitnessService fitnessService;

    @GetMapping("/exercises")
    public Page<Exercise> listExercises(Pageable pageable) {
        return fitnessService.findAllExercises(pageable);
    }

    @GetMapping("/exercises/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        return ResponseEntity.ok(fitnessService.findExerciseById(id));
    }

    @PostMapping("/exercises")
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) {
        return ResponseEntity.ok(fitnessService.saveExercise(exercise));
    }

    @PutMapping("/exercises/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Long id, @Valid @RequestBody Exercise exercise) {
        exercise.setId(id);
        return ResponseEntity.ok(fitnessService.saveExercise(exercise));
    }

    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        fitnessService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/training-programs")
    public Page<TrainingProgram> listPrograms(
            @RequestParam(required = false) Long userId, Pageable pageable) {
        return userId != null
                ? fitnessService.findProgramsByUser(userId, pageable)
                : fitnessService.findAllPrograms(pageable);
    }

    @GetMapping("/training-programs/{id}")
    public ResponseEntity<TrainingProgram> getProgram(@PathVariable Long id) {
        return ResponseEntity.ok(fitnessService.findProgramById(id));
    }

    @PostMapping("/training-programs")
    public ResponseEntity<TrainingProgram> createProgram(@Valid @RequestBody TrainingProgram program) {
        return ResponseEntity.ok(fitnessService.saveProgram(program));
    }

    @PutMapping("/training-programs/{id}")
    public ResponseEntity<TrainingProgram> updateProgram(@PathVariable Long id, @Valid @RequestBody TrainingProgram program) {
        program.setId(id);
        return ResponseEntity.ok(fitnessService.saveProgram(program));
    }

    @DeleteMapping("/training-programs/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        fitnessService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
