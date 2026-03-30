package com.example.ETPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * PerformanceReview entity representing a review/rating for an employee.
 * Rating is on a scale of 1 to 5.
 */
@Entity
@Table(name = "performance_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Feedback is required")
    @Size(max = 2000, message = "Feedback cannot exceed 2000 characters")
    @Column(length = 2000, nullable = false)
    private String feedback;

    @NotNull(message = "Review date is required")
    @Column(nullable = false)
    private LocalDate reviewDate;

    // Many Reviews belong to one Employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
