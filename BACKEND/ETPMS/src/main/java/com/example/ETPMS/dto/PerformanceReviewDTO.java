package com.example.ETPMS.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Data Transfer Object for PerformanceReview entity.
 * Uses employeeId instead of nested Employee object.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReviewDTO {

    private Long id;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @NotBlank(message = "Feedback is required")
    @Size(max = 2000, message = "Feedback cannot exceed 2000 characters")
    private String feedback;

    @NotNull(message = "Review date is required")
    private LocalDate reviewDate;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    // Employee name for display purposes (read-only)
    private String employeeName;
}
