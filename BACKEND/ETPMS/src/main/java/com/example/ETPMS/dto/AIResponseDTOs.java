package com.example.ETPMS.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;
import java.util.Map;

public class AIResponseDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformancePredictionRequest {
        @JsonProperty("employee_id")
        private Long employeeId;
        @JsonProperty("tasks_assigned")
        private int tasksAssigned;
        @JsonProperty("tasks_completed")
        private int tasksCompleted;
        @JsonProperty("missed_deadlines")
        private int missedDeadlines;
        @JsonProperty("avg_completion_time")
        private double avgCompletionTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerformancePredictionDTO {
        @JsonAlias("employee_id")
        private Long employeeId;
        @JsonAlias("predicted_score")
        private double predictedScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskCompletionRequest {
        @JsonProperty("task_id")
        private Long taskId;
        private int difficulty;
        @JsonProperty("deadline_days")
        private int deadlineDays;
        @JsonProperty("employee_workload")
        private int employeeWorkload;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskCompletionDTO {
        @JsonProperty("task_id")
        private Long taskId;
        private String prediction;
        private double confidence;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeClusterItem {
        @JsonAlias("employee_id")
        private Long employeeId;
        private String cluster;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeClusterResponse {
        private List<EmployeeClusterItem> clusters;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskRecommendationRequest {
        @JsonProperty("task_id")
        private Long taskId;
        private int difficulty;
        @JsonProperty("deadline_days")
        private int deadlineDays;
        @JsonProperty("available_employees")
        private List<Integer> availableEmployees;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskRecommendationDTO {
        @JsonProperty("task_id")
        private Long taskId;
        @JsonAlias("recommended_employee_id")
        private Long recommendedEmployee;
        @JsonAlias("recommendation_score")
        private double recommendationScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutoPerformanceScoreRequest {
        private List<Map<String, Object>> employees;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutoPerformanceScoreItem {
        @JsonAlias("employee_id")
        private Long employeeId;
        @JsonAlias("performance_score")
        private double performanceScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AutoPerformanceScoreResponse {
        private List<AutoPerformanceScoreItem> scores;
    }
}
