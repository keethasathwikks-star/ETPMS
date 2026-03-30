package com.example.ETPMS.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;

/**
 * Data Transfer Object for Task entity.
 * Uses employeeId instead of nested Employee object.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    @NotNull(message = "Status is required")
    private String status; // PENDING, IN_PROGRESS, COMPLETED

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    // Employee name for display purposes (read-only)
    private String employeeName;
}
