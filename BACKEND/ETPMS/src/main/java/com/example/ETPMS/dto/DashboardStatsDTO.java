package com.example.ETPMS.dto;

import lombok.*;

/**
 * DTO for dashboard statistics displayed on the frontend.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    private long totalEmployees;
    private long totalTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long completedTasks;
    private long totalReviews;
    private double averageRating;
}
