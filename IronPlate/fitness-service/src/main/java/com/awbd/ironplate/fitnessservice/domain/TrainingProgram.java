package com.awbd.ironplate.fitnessservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "training_programs")
@Data
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @Min(1) @Max(52)
    private Integer durationWeeks;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    private Long userId;

    public enum DifficultyLevel { BEGINNER, INTERMEDIATE, ADVANCED }
}
