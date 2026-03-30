package com.example.ETPMS.controller;

import com.example.ETPMS.dto.AIResponseDTOs.*;
import com.example.ETPMS.service.AiAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for AI Analytics APIs
 */
@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AiAnalyticsController {

    private final AiAnalyticsService aiAnalyticsService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        try {
            Map<String, String> response = new HashMap<>();
            if (aiAnalyticsService.isAiServiceAvailable()) {
                response.put("status", "healthy");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "unavailable");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
        } catch (Exception e) {
            log.error("Health check error: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/performance/{employee}")
    public ResponseEntity<PerformancePredictionDTO> predictPerformance(@PathVariable("employee") Long employee) {
        try {
            log.info("Predicting performance for employee: {}", employee);
            PerformancePredictionDTO result = aiAnalyticsService.predictPerformance(employee);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error predicting performance: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/task-prediction/{taskId}")
    public ResponseEntity<TaskCompletionDTO> predictTaskCompletion(@PathVariable Long taskId) {
        try {
            log.info("Predicting task completion for task: {}", taskId);
            TaskCompletionDTO result = aiAnalyticsService.predictTaskCompletion(taskId, 5);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error predicting task completion: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/employee-clusters")
    public ResponseEntity<EmployeeClusterResponse> getEmployeeClusters() {
        try {
            log.info("Getting employee clusters");
            EmployeeClusterResponse result = aiAnalyticsService.getEmployeeClusters();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getting clusters: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/recommend-employee/{taskId}")
    public ResponseEntity<TaskRecommendationDTO> recommendEmployee(@PathVariable Long taskId) {
        try {
            log.info("Getting recommendation for task: {}", taskId);
            TaskRecommendationDTO result = aiAnalyticsService.recommendEmployee(taskId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error getting recommendation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/performance-scores")
    public ResponseEntity<AutoPerformanceScoreResponse> getPerformanceScores() {
        try {
            log.info("Calculating performance scores");
            AutoPerformanceScoreResponse result = aiAnalyticsService.calculatePerformanceScores();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error calculating scores: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
