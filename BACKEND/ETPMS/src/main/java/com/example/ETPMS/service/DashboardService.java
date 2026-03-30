package com.example.ETPMS.service;

import com.example.ETPMS.dto.DashboardStatsDTO;
import com.example.ETPMS.model.TaskStatus;
import com.example.ETPMS.repository.EmployeeRepository;
import com.example.ETPMS.repository.PerformanceReviewRepository;
import com.example.ETPMS.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for dashboard statistics aggregation.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final PerformanceReviewRepository reviewRepository;

    /**
     * Gather all dashboard statistics into a single DTO.
     */
    public DashboardStatsDTO getStats() {
        return DashboardStatsDTO.builder()
                .totalEmployees(employeeRepository.count())
                .totalTasks(taskRepository.count())
                .pendingTasks(taskRepository.countByStatus(TaskStatus.PENDING))
                .inProgressTasks(taskRepository.countByStatus(TaskStatus.IN_PROGRESS))
                .completedTasks(taskRepository.countByStatus(TaskStatus.COMPLETED))
                .totalReviews(reviewRepository.count())
                .averageRating(reviewRepository.findAverageRating())
                .build();
    }
}
