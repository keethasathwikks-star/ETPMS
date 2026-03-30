package com.example.ETPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Employee entity representing a company employee.
 * One Employee can have multiple Tasks and PerformanceReviews.
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotBlank(message = "Role is required")
    @Column(nullable = false)
    private String role;

    // One Employee can have many Tasks
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    // One Employee can have many Performance Reviews
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PerformanceReview> performanceReviews = new ArrayList<>();
}
