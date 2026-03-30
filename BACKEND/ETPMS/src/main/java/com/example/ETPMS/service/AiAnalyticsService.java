package com.example.ETPMS.service;

import com.example.ETPMS.dto.AIResponseDTOs.*;
import com.example.ETPMS.model.Employee;
import com.example.ETPMS.model.Task;
import com.example.ETPMS.model.TaskStatus;
import com.example.ETPMS.repository.EmployeeRepository;
import com.example.ETPMS.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;

/**
 * Service for integrating with AI microservice
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiAnalyticsService {
    private final RestTemplate restTemplate;
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    public PerformancePredictionDTO predictPerformance(Long employee) {
        try {
            PerformancePredictionRequest request = new PerformancePredictionRequest();
            request.setEmployeeId(employee);
            request.setTasksAssigned(5);
            request.setTasksCompleted(3);
            request.setMissedDeadlines(1);
            request.setAvgCompletionTime(10.0);
            
            log.info("Calling AI service: {}/predict-performance", aiServiceUrl);
            PerformancePredictionDTO response = restTemplate.postForObject(
                    aiServiceUrl + "/predict-performance", request, PerformancePredictionDTO.class);
            log.info("Performance prediction response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error predicting performance: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    public TaskCompletionDTO predictTaskCompletion(Long taskId, int employeeWorkload) {
        try {
            TaskCompletionRequest request = new TaskCompletionRequest();
            request.setTaskId(taskId);
            request.setDifficulty(3);
            request.setDeadlineDays(7);
            request.setEmployeeWorkload(employeeWorkload);
            
            log.info("Calling AI service: {}/predict-task-completion", aiServiceUrl);
            TaskCompletionDTO response = restTemplate.postForObject(
                    aiServiceUrl + "/predict-task-completion", request, TaskCompletionDTO.class);
            log.info("Task completion response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error predicting task completion: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    public EmployeeClusterResponse getEmployeeClusters() {
        try {
            List<Map<String, Object>> employeeData = buildEmployeeStats();
            
            log.info("Calling AI service: {}/employee-clusters", aiServiceUrl);
            EmployeeClusterResponse response = restTemplate.postForObject(
                    aiServiceUrl + "/employee-clusters", employeeData, EmployeeClusterResponse.class);
            log.info("Clusters response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error getting clusters: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    public TaskRecommendationDTO recommendEmployee(Long taskId) {
        try {
            TaskRecommendationRequest request = new TaskRecommendationRequest();
            request.setTaskId(taskId);
            request.setDifficulty(3);
            request.setDeadlineDays(7);
            request.setAvailableEmployees(getAvailableEmployeeIds());
            
            log.info("Calling AI service: {}/recommend-employee", aiServiceUrl);
            TaskRecommendationDTO response = restTemplate.postForObject(
                    aiServiceUrl + "/recommend-employee", request, TaskRecommendationDTO.class);
            log.info("Recommendation response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error recommending employee: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    public AutoPerformanceScoreResponse calculatePerformanceScores() {
        try {
            AutoPerformanceScoreRequest request = new AutoPerformanceScoreRequest();
            request.setEmployees(buildEmployeeStats());
            
            log.info("Calling AI service: {}/calculate-performance-scores", aiServiceUrl);
            AutoPerformanceScoreResponse response = restTemplate.postForObject(
                    aiServiceUrl + "/calculate-performance-scores", request, AutoPerformanceScoreResponse.class);
            log.info("Scores response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error calculating scores: {}", e.getMessage(), e);
            throw new RuntimeException("AI service error: " + e.getMessage(), e);
        }
    }

    public boolean isAiServiceAvailable() {
        try {
            String url = aiServiceUrl + "/health";
            log.info("Checking AI health at: {}", url);
            Map response = restTemplate.getForObject(url, Map.class);
            boolean healthy = response != null && "healthy".equals(response.get("status"));
            log.info("AI service health: {}", healthy);
            return healthy;
        } catch (Exception e) {
            log.warn("AI service unavailable: {}", e.getMessage());
            return false;
        }
    }

    private List<Integer> getAvailableEmployeeIds() {
        List<Employee> employees = employeeRepository.findAll();
        List<Integer> ids = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getId() != null) {
                ids.add(employee.getId().intValue());
            }
        }
        return ids;
    }

    private List<Map<String, Object>> buildEmployeeStats() {
        List<Employee> employees = employeeRepository.findAll();
        List<Map<String, Object>> stats = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (Employee employee : employees) {
            List<Task> tasks = taskRepository.findByEmployeeId(employee.getId());
            int tasksAssigned = tasks.size();
            int tasksCompleted = (int) tasks.stream()
                    .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                    .count();
            int missedDeadlines = (int) tasks.stream()
                    .filter(task -> task.getStatus() != TaskStatus.COMPLETED && task.getDeadline().isBefore(today))
                    .count();
            double avgCompletionTime = tasksAssigned > 0 ? 7.0 : 7.0;

            Map<String, Object> employeeMap = new HashMap<>();
            employeeMap.put("id", employee.getId());
            employeeMap.put("tasksAssigned", tasksAssigned);
            employeeMap.put("tasksCompleted", tasksCompleted);
            employeeMap.put("missedDeadlines", missedDeadlines);
            employeeMap.put("avgCompletionTime", avgCompletionTime);
            stats.add(employeeMap);
        }
        return stats;
    }
}
