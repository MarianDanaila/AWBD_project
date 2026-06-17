package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_profiles")
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double height;
    private Double weight;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private FitnessGoal fitnessGoal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum FitnessGoal {
        BULK, CUT, MAINTAIN
    }
}
