# ETPMS AI Service - Example Payloads & Integration Guide

Complete examples of all request/response payloads and integration scenarios.

---

## 📦 Example Request/Response Payloads

### 1. Performance Prediction

#### Scenario: Predict performance for a high-performing employee

**Request:**
```json
{
  "employee_id": 5,
  "tasks_assigned": 25,
  "tasks_completed": 24,
  "missed_deadlines": 1,
  "avg_completion_time": 8.5
}
```

**Response:**
```json
{
  "employee_id": 5,
  "predicted_score": 92.50
}
```

---

#### Scenario: Predict performance for an underperforming employee

**Request:**
```json
{
  "employee_id": 12,
  "tasks_assigned": 20,
  "tasks_completed": 10,
  "missed_deadlines": 8,
  "avg_completion_time": 25.0
}
```

**Response:**
```json
{
  "employee_id": 12,
  "predicted_score": 35.20
}
```

---

### 2. Task Completion Prediction

#### Scenario: Easy task with plenty of time

**Request:**
```json
{
  "task_id": 101,
  "difficulty": 1,
  "deadline_days": 30,
  "employee_workload": 2
}
```

**Response:**
```json
{
  "task_id": 101,
  "prediction": "ON_TIME",
  "confidence": 0.9567
}
```

---

#### Scenario: Difficult task with tight deadline and high workload

**Request:**
```json
{
  "task_id": 102,
  "difficulty": 5,
  "deadline_days": 3,
  "employee_workload": 18
}
```

**Response:**
```json
{
  "task_id": 102,
  "prediction": "LATE",
  "confidence": 0.8234
}
```

---

### 3. Employee Clustering

#### Scenario: Cluster 5 employees

**Request:**
```json
[
  {
    "id": 1,
    "tasksCompleted": 50,
    "missedDeadlines": 1,
    "avgCompletionTime": 7.0
  },
  {
    "id": 2,
    "tasksCompleted": 35,
    "missedDeadlines": 3,
    "avgCompletionTime": 12.5
  },
  {
    "id": 3,
    "tasksCompleted": 20,
    "missedDeadlines": 7,
    "avgCompletionTime": 18.0
  },
  {
    "id": 4,
    "tasksCompleted": 45,
    "missedDeadlines": 2,
    "avgCompletionTime": 8.5
  },
  {
    "id": 5,
    "tasksCompleted": 10,
    "missedDeadlines": 12,
    "avgCompletionTime": 25.0
  }
]
```

**Response:**
```json
{
  "clusters": [
    {
      "employee_id": 1,
      "cluster": "High Performer"
    },
    {
      "employee_id": 2,
      "cluster": "Medium Performer"
    },
    {
      "employee_id": 3,
      "cluster": "Medium Performer"
    },
    {
      "employee_id": 4,
      "cluster": "High Performer"
    },
    {
      "employee_id": 5,
      "cluster": "Low Performer"
    }
  ]
}
```

---

### 4. Task Recommendation

#### Scenario: Find best employee for urgent, difficult task

**Request:**
```json
{
  "task_id": 150,
  "difficulty": 4,
  "deadline_days": 5,
  "available_employees": [2, 5, 7, 9, 11]
}
```

**Response:**
```json
{
  "task_id": 150,
  "recommended_employee_id": 7,
  "recommendation_score": 0.8876
}
```

---

#### Scenario: Find best employee for routine task with flexible timeline

**Request:**
```json
{
  "task_id": 151,
  "difficulty": 1,
  "deadline_days": 30,
  "available_employees": [3, 8, 10, 13, 14]
}
```

**Response:**
```json
{
  "task_id": 151,
  "recommended_employee_id": 8,
  "recommendation_score": 0.9234
}
```

---

### 5. Performance Score Calculation

#### Scenario: Calculate scores for entire department

**Request:**
```json
{
  "employees": [
    {
      "id": 1,
      "tasksAssigned": 20,
      "tasksCompleted": 20,
      "missedDeadlines": 0,
      "avgCompletionTime": 8.0
    },
    {
      "id": 2,
      "tasksAssigned": 20,
      "tasksCompleted": 15,
      "missedDeadlines": 3,
      "avgCompletionTime": 15.0
    },
    {
      "id": 3,
      "tasksAssigned": 20,
      "tasksCompleted": 10,
      "missedDeadlines": 8,
      "avgCompletionTime": 22.0
    },
    {
      "id": 4,
      "tasksAssigned": 0,
      "tasksCompleted": 0,
      "missedDeadlines": 0,
      "avgCompletionTime": 0
    }
  ]
}
```

**Response:**
```json
{
  "scores": [
    {
      "employee_id": 1,
      "performance_score": 100.00
    },
    {
      "employee_id": 2,
      "performance_score": 68.75
    },
    {
      "employee_id": 3,
      "performance_score": 41.67
    },
    {
      "employee_id": 4,
      "performance_score": 0.00
    }
  ]
}
```

---

## 🔌 Integration Examples

### Python Integration Example

```python
import requests
import json

# Configuration
AI_SERVICE_URL = "http://localhost:8000"

class ETMPSAIClient:
    def __init__(self, base_url=AI_SERVICE_URL):
        self.base_url = base_url
    
    def predict_performance(self, employee_id, tasks_assigned, tasks_completed, 
                           missed_deadlines, avg_completion_time):
        """Predict employee performance score"""
        payload = {
            "employee_id": employee_id,
            "tasks_assigned": tasks_assigned,
            "tasks_completed": tasks_completed,
            "missed_deadlines": missed_deadlines,
            "avg_completion_time": avg_completion_time
        }
        response = requests.post(f"{self.base_url}/predict-performance", json=payload)
        return response.json()
    
    def predict_task_completion(self, task_id, difficulty, deadline_days, employee_workload):
        """Predict task completion status"""
        payload = {
            "task_id": task_id,
            "difficulty": difficulty,
            "deadline_days": deadline_days,
            "employee_workload": employee_workload
        }
        response = requests.post(f"{self.base_url}/predict-task-completion", json=payload)
        return response.json()
    
    def get_employee_clusters(self, employees):
        """Cluster employees by performance"""
        response = requests.post(f"{self.base_url}/employee-clusters", json=employees)
        return response.json()
    
    def recommend_employee(self, task_id, difficulty, deadline_days, available_employees):
        """Recommend employee for task"""
        payload = {
            "task_id": task_id,
            "difficulty": difficulty,
            "deadline_days": deadline_days,
            "available_employees": available_employees
        }
        response = requests.post(f"{self.base_url}/recommend-employee", json=payload)
        return response.json()
    
    def calculate_performance_scores(self, employees):
        """Calculate performance scores"""
        payload = {"employees": employees}
        response = requests.post(f"{self.base_url}/calculate-performance-scores", json=payload)
        return response.json()

# Usage
if __name__ == "__main__":
    client = ETMPSAIClient()
    
    # Example 1: Predict performance
    result = client.predict_performance(
        employee_id=5,
        tasks_assigned=10,
        tasks_completed=9,
        missed_deadlines=1,
        avg_completion_time=12.5
    )
    print("Performance Prediction:", result)
    
    # Example 2: Predict task completion
    result = client.predict_task_completion(
        task_id=22,
        difficulty=3,
        deadline_days=10,
        employee_workload=5
    )
    print("Task Completion Prediction:", result)
```

---

### JavaScript/Node.js Integration Example

```javascript
const axios = require('axios');

class ETMPSAIClient {
  constructor(baseUrl = 'http://localhost:8000') {
    this.baseUrl = baseUrl;
    this.client = axios.create({
      baseURL: baseUrl,
      timeout: 10000
    });
  }

  async predictPerformance(employeeId, tasksAssigned, tasksCompleted, missedDeadlines, avgCompletionTime) {
    const payload = {
      employee_id: employeeId,
      tasks_assigned: tasksAssigned,
      tasks_completed: tasksCompleted,
      missed_deadlines: missedDeadlines,
      avg_completion_time: avgCompletionTime
    };
    try {
      const response = await this.client.post('/predict-performance', payload);
      return response.data;
    } catch (error) {
      console.error('Error predicting performance:', error.message);
      throw error;
    }
  }

  async predictTaskCompletion(taskId, difficulty, deadlineDays, employeeWorkload) {
    const payload = {
      task_id: taskId,
      difficulty: difficulty,
      deadline_days: deadlineDays,
      employee_workload: employeeWorkload
    };
    try {
      const response = await this.client.post('/predict-task-completion', payload);
      return response.data;
    } catch (error) {
      console.error('Error predicting task completion:', error.message);
      throw error;
    }
  }

  async getEmployeeClusters(employees) {
    try {
      const response = await this.client.post('/employee-clusters', employees);
      return response.data;
    } catch (error) {
      console.error('Error getting clusters:', error.message);
      throw error;
    }
  }

  async recommendEmployee(taskId, difficulty, deadlineDays, availableEmployees) {
    const payload = {
      task_id: taskId,
      difficulty: difficulty,
      deadline_days: deadlineDays,
      available_employees: availableEmployees
    };
    try {
      const response = await this.client.post('/recommend-employee', payload);
      return response.data;
    } catch (error) {
      console.error('Error recommending employee:', error.message);
      throw error;
    }
  }

  async calculatePerformanceScores(employees) {
    const payload = { employees };
    try {
      const response = await this.client.post('/calculate-performance-scores', payload);
      return response.data;
    } catch (error) {
      console.error('Error calculating scores:', error.message);
      throw error;
    }
  }
}

// Usage
(async () => {
  const client = new ETMPSAIClient();

  // Example 1: Predict performance
  const perfResult = await client.predictPerformance(5, 10, 9, 1, 12.5);
  console.log('Performance Result:', perfResult);

  // Example 2: Get clusters
  const employees = [
    { id: 1, tasksCompleted: 45, missedDeadlines: 2, avgCompletionTime: 8.5 },
    { id: 2, tasksCompleted: 20, missedDeadlines: 8, avgCompletionTime: 20.0 }
  ];
  const clusterResult = await client.getEmployeeClusters(employees);
  console.log('Cluster Result:', clusterResult);
})();
```

---

### Java Integration Example

```java
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ETMPSAIClientJava {
    private RestTemplate restTemplate;
    private String baseUrl = "http://localhost:8000";
    private ObjectMapper mapper = new ObjectMapper();

    public ETMPSAIClientJava(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PerformancePredictionDTO predictPerformance(
            Long employeeId, int tasksAssigned, int tasksCompleted,
            int missedDeadlines, double avgCompletionTime) {
        
        PerformancePredictionRequest request = PerformancePredictionRequest.builder()
                .employeeId(employeeId)
                .tasksAssigned(tasksAssigned)
                .tasksCompleted(tasksCompleted)
                .missedDeadlines(missedDeadlines)
                .avgCompletionTime(avgCompletionTime)
                .build();

        return restTemplate.postForObject(
                baseUrl + "/predict-performance",
                request,
                PerformancePredictionDTO.class
        );
    }

    public TaskCompletionDTO predictTaskCompletion(
            Long taskId, int difficulty, int deadlineDays, int employeeWorkload) {
        
        TaskCompletionRequest request = TaskCompletionRequest.builder()
                .taskId(taskId)
                .difficulty(difficulty)
                .deadlineDays(deadlineDays)
                .employeeWorkload(employeeWorkload)
                .build();

        return restTemplate.postForObject(
                baseUrl + "/predict-task-completion",
                request,
                TaskCompletionDTO.class
        );
    }
}
```

---

## 📊 Batch Processing Example

### Processing Multiple Employees

**Use Case:** Calculate performance scores for all employees in a department

```bash
curl -X POST http://localhost:8000/calculate-performance-scores \
  -H "Content-Type: application/json" \
  -d '{
    "employees": [
      {"id": 1, "tasksAssigned": 20, "tasksCompleted": 20, "missedDeadlines": 0, "avgCompletionTime": 8.0},
      {"id": 2, "tasksAssigned": 20, "tasksCompleted": 18, "missedDeadlines": 1, "avgCompletionTime": 10.5},
      {"id": 3, "tasksAssigned": 20, "tasksCompleted": 15, "missedDeadlines": 3, "avgCompletionTime": 15.0},
      {"id": 4, "tasksAssigned": 20, "tasksCompleted": 10, "missedDeadlines": 8, "avgCompletionTime": 22.0},
      {"id": 5, "tasksAssigned": 20, "tasksCompleted": 20, "missedDeadlines": 0, "avgCompletionTime": 7.5}
    ]
  }'
```

---

### Processing Multiple Task Predictions

**Use Case:** Predict completion status for all pending tasks

```python
import requests

tasks = [
    {"task_id": 1, "difficulty": 1, "deadline_days": 30, "employee_workload": 2},
    {"task_id": 2, "difficulty": 3, "deadline_days": 10, "employee_workload": 8},
    {"task_id": 3, "difficulty": 5, "deadline_days": 3, "employee_workload": 15},
    {"task_id": 4, "difficulty": 2, "deadline_days": 20, "employee_workload": 4},
    {"task_id": 5, "difficulty": 4, "deadline_days": 7, "employee_workload": 10}
]

predictions = []
for task in tasks:
    response = requests.post(
        "http://localhost:8000/predict-task-completion",
        json=task
    )
    predictions.append(response.json())

# Results
for pred in predictions:
    print(f"Task {pred['task_id']}: {pred['prediction']} (confidence: {pred['confidence']})")
```

---

## 🚀 Real-World Scenarios

### Scenario 1: Employee Performance Dashboard

**Flow:**
1. Fetch all employees from database
2. Get performance clusters: `POST /employee-clusters`
3. For each employee, get auto score: `POST /calculate-performance-scores`
4. Display results in dashboard

```python
# Get all employees
employees_df = pd.read_sql("SELECT * FROM employees", db_connection)

# Cluster employees
cluster_response = requests.post(
    "http://localhost:8000/employee-clusters",
    json=employees_df.to_dict('records')
)

# Calculate scores
score_response = requests.post(
    "http://localhost:8000/calculate-performance-scores",
    json={"employees": employees_df.to_dict('records')}
)

# Combine results
results = {}
for cluster in cluster_response.json()['clusters']:
    emp_id = cluster['employee_id']
    score = next(s['performance_score'] for s in score_response.json()['scores'] if s['employee_id'] == emp_id)
    results[emp_id] = {
        'cluster': cluster['cluster'],
        'score': score
    }
```

---

### Scenario 2: Smart Task Assignment

**Flow:**
1. New task created
2. Calculate task difficulty
3. Get recommended employee: `POST /recommend-employee`
4. Auto-assign task to recommended employee

```java
// When creating a new task
Task newTask = new Task(...);

TaskRecommendationDTO recommendation = aiAnalyticsService.recommendEmployee(newTask.getId());

Employee recommendedEmployee = employeeRepository.findById(
    recommendation.getRecommendedEmployeeId()
).orElse(null);

if (recommendedEmployee != null) {
    newTask.setEmployee(recommendedEmployee);
    taskRepository.save(newTask);
    notificationService.sendNotification(
        recommendedEmployee.getEmail(),
        "New task assigned: " + newTask.getTitle()
    );
}
```

---

### Scenario 3: Task Risk Assessment

**Flow:**
1. For each pending task
2. Predict if it will be late: `POST /predict-task-completion`
3. If predicted LATE, escalate or reassignment

```python
pending_tasks = db.query("SELECT * FROM tasks WHERE status='PENDING'")

for task in pending_tasks:
    prediction = requests.post(
        "http://localhost:8000/predict-task-completion",
        json={
            "task_id": task.id,
            "difficulty": task.difficulty,
            "deadline_days": (task.deadline - today).days,
            "employee_workload": task.employee.current_tasks
        }
    ).json()
    
    if prediction['prediction'] == 'LATE' and prediction['confidence'] > 0.7:
        # Send alert
        alert_manager.create_alert(
            task_id=task.id,
            message=f"Task {task.title} at risk of missing deadline",
            severity="HIGH"
        )
```

---

## ✅ Validation Checklist

- [ ] AI service running on port 8000
- [ ] Models trained and in `models/` directory
- [ ] `/health` endpoint returns "healthy"
- [ ] Spring Boot configured with `ai.service.url`
- [ ] Backend successfully calls AI endpoints
- [ ] Frontend receives predictions in real-time
- [ ] Error handling implemented
- [ ] Response times acceptable (<1s per prediction)

---

**Version:** 1.0.0  
**Created:** March 16, 2026
