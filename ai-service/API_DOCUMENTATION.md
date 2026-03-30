# ETPMS AI Service - API Documentation

Complete API reference for the ETPMS AI microservice and Spring Boot integration.

---

## 📌 Base URLs

- **AI Service:** `http://localhost:8000`
- **Spring Boot Backend:** `http://localhost:8080`

---

## 🏥 Health & Status

### Check AI Service Status

**Request:**
```http
GET /health
```

**Response (Success):**
```json
{
  "status": "healthy",
  "models_available": true,
  "message": "All models loaded"
}
```

**Response (Models Not Trained):**
```json
{
  "status": "models_not_trained",
  "models_available": false,
  "message": "Run 'python train_models.py' to train models"
}
```

---

## 1️⃣ Performance Prediction API

### Predict Employee Performance Score

**Endpoint:** `POST /predict-performance`

**Description:** Predicts an employee's performance score (0-100) using Linear Regression based on task metrics.

**Request Body:**
```json
{
  "employee_id": 5,
  "tasks_assigned": 10,
  "tasks_completed": 9,
  "missed_deadlines": 1,
  "avg_completion_time": 12.5
}
```

**Parameters:**
| Field | Type | Description |
|-------|------|-------------|
| `employee_id` | integer | ID of the employee |
| `tasks_assigned` | integer | Total tasks assigned to employee |
| `tasks_completed` | integer | Number of completed tasks |
| `missed_deadlines` | integer | Number of missed deadlines |
| `avg_completion_time` | float | Average task completion time (days) |

**Response:**
```json
{
  "employee_id": 5,
  "predicted_score": 88.45
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8000/predict-performance \
  -H "Content-Type: application/json" \
  -d '{
    "employee_id": 5,
    "tasks_assigned": 10,
    "tasks_completed": 9,
    "missed_deadlines": 1,
    "avg_completion_time": 12.5
  }'
```

**Error Response:**
```json
{
  "detail": "Model 'performance_predictor' not found. Run train_models.py first."
}
```

---

### Batch Performance Predictions

**Endpoint:** `POST /batch-predict-performance`

**Description:** Predict performance for multiple employees in a single request.

**Request Body:**
```json
[
  {
    "employee_id": 1,
    "tasks_assigned": 10,
    "tasks_completed": 9,
    "missed_deadlines": 1,
    "avg_completion_time": 12.5
  },
  {
    "employee_id": 2,
    "tasks_assigned": 15,
    "tasks_completed": 10,
    "missed_deadlines": 5,
    "avg_completion_time": 20.0
  }
]
```

**Response:**
```json
[
  {
    "employee_id": 1,
    "predicted_score": 88.45
  },
  {
    "employee_id": 2,
    "predicted_score": 45.67
  }
]
```

---

## 2️⃣ Task Completion Prediction API

### Predict Task Completion Status

**Endpoint:** `POST /predict-task-completion`

**Description:** Predicts whether a task will be completed on time or late using Logistic Regression.

**Request Body:**
```json
{
  "task_id": 22,
  "difficulty": 3,
  "deadline_days": 10,
  "employee_workload": 5
}
```

**Parameters:**
| Field | Type | Description |
|-------|------|-------------|
| `task_id` | integer | ID of the task |
| `difficulty` | integer | Task difficulty (1-5 scale) |
| `deadline_days` | integer | Days remaining until deadline |
| `employee_workload` | integer | Current workload (0-20 scale) |

**Response:**
```json
{
  "task_id": 22,
  "prediction": "ON_TIME",
  "confidence": 0.8234
}
```

**Response Predictions:**
- `"ON_TIME"` - Task likely to be completed before deadline
- `"LATE"` - Task likely to miss deadline

**cURL Example:**
```bash
curl -X POST http://localhost:8000/predict-task-completion \
  -H "Content-Type: application/json" \
  -d '{
    "task_id": 22,
    "difficulty": 3,
    "deadline_days": 10,
    "employee_workload": 5
  }'
```

---

## 3️⃣ Employee Clustering API

### Cluster Employees into Performance Tiers

**Endpoint:** `POST /employee-clusters`

**Description:** Groups employees into performance tiers (High, Medium, Low) using K-Means clustering.

**Request Body:**
```json
[
  {
    "id": 1,
    "tasksCompleted": 45,
    "missedDeadlines": 2,
    "avgCompletionTime": 8.5
  },
  {
    "id": 2,
    "tasksCompleted": 28,
    "missedDeadlines": 5,
    "avgCompletionTime": 15.0
  },
  {
    "id": 3,
    "tasksCompleted": 15,
    "missedDeadlines": 10,
    "avgCompletionTime": 22.0
  }
]
```

**Parameters (per employee):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | integer | Employee ID |
| `tasksCompleted` | integer | Number of completed tasks |
| `missedDeadlines` | integer | Number of missed deadlines |
| `avgCompletionTime` | float | Average completion time (days) |

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
      "cluster": "Low Performer"
    }
  ]
}
```

**Cluster Categories:**
- `"High Performer"` - High completion rate, few missed deadlines
- `"Medium Performer"` - Moderate performance metrics
- `"Low Performer"` - Low completion rate, many missed deadlines

**cURL Example:**
```bash
curl -X POST http://localhost:8000/employee-clusters \
  -H "Content-Type: application/json" \
  -d '[
    {"id": 1, "tasksCompleted": 45, "missedDeadlines": 2, "avgCompletionTime": 8.5},
    {"id": 2, "tasksCompleted": 28, "missedDeadlines": 5, "avgCompletionTime": 15.0},
    {"id": 3, "tasksCompleted": 15, "missedDeadlines": 10, "avgCompletionTime": 22.0}
  ]'
```

---

## 4️⃣ Task Recommendation API

### Recommend Best Employee for Task

**Endpoint:** `POST /recommend-employee`

**Description:** Uses Nearest Neighbors algorithm to recommend the best employee for a task.

**Request Body:**
```json
{
  "task_id": 22,
  "difficulty": 3,
  "deadline_days": 10,
  "available_employees": [1, 2, 5, 7, 9]
}
```

**Parameters:**
| Field | Type | Description |
|-------|------|-------------|
| `task_id` | integer | ID of the task |
| `difficulty` | integer | Task difficulty (1-5) |
| `deadline_days` | integer | Days until deadline |
| `available_employees` | array | List of available employee IDs |

**Response:**
```json
{
  "task_id": 22,
  "recommended_employee_id": 5,
  "recommendation_score": 0.9234
}
```

**Interpretation:**
- `recommendation_score`: 0.0 to 1.0 (higher = better match)
- `recommended_employee_id`: ID of best-fit employee

**cURL Example:**
```bash
curl -X POST http://localhost:8000/recommend-employee \
  -H "Content-Type: application/json" \
  -d '{
    "task_id": 22,
    "difficulty": 3,
    "deadline_days": 10,
    "available_employees": [1, 2, 5, 7, 9]
  }'
```

---

## 5️⃣ Performance Score Calculation API

### Calculate Automatic Performance Scores

**Endpoint:** `POST /calculate-performance-scores`

**Description:** Calculates performance scores using a predefined formula that considers completion ratio, deadline misses, and completion speed.

**Formula:**
```
score = (tasks_completed / tasks_assigned) * 50 +
        (1 - missed_deadlines / tasks_assigned) * 30 +
        (speed_score) * 20
```

**Request Body:**
```json
{
  "employees": [
    {
      "id": 1,
      "tasksAssigned": 10,
      "tasksCompleted": 9,
      "missedDeadlines": 1,
      "avgCompletionTime": 12.5
    },
    {
      "id": 2,
      "tasksAssigned": 15,
      "tasksCompleted": 10,
      "missedDeadlines": 5,
      "avgCompletionTime": 20.0
    }
  ]
}
```

**Parameters (per employee):**
| Field | Type | Description |
|-------|------|-------------|
| `id` | integer | Employee ID |
| `tasksAssigned` | integer | Total tasks assigned |
| `tasksCompleted` | integer | Completed tasks count |
| `missedDeadlines` | integer | Missed deadline count |
| `avgCompletionTime` | float | Average time per task (days) |

**Response:**
```json
{
  "scores": [
    {
      "employee_id": 1,
      "performance_score": 78.33
    },
    {
      "employee_id": 2,
      "performance_score": 45.67
    }
  ]
}
```

**Score Interpretation:**
- 90-100: Excellent
- 75-90: Good
- 60-75: Average
- Below 60: Needs Improvement

**cURL Example:**
```bash
curl -X POST http://localhost:8000/calculate-performance-scores \
  -H "Content-Type: application/json" \
  -d '{
    "employees": [
      {"id": 1, "tasksAssigned": 10, "tasksCompleted": 9, "missedDeadlines": 1, "avgCompletionTime": 12.5},
      {"id": 2, "tasksAssigned": 15, "tasksCompleted": 10, "missedDeadlines": 5, "avgCompletionTime": 20.0}
    ]
  }'
```

---

## 🔗 Spring Boot Backend Integration APIs

### All Spring Boot AI endpoints forward to the Python AI service and return the results.

---

### 1. Predict Employee Performance (via Spring Boot)

**Endpoint:** `POST /api/ai/performance/{employeeId}`

**URL:** `http://localhost:8080/api/ai/performance/5`

**What it does:**
1. Fetches employee data from MySQL
2. Calls AI service `/predict-performance`
3. Returns prediction to client

**Response:**
```json
{
  "employeeId": 5,
  "predictedScore": 88.45
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/ai/performance/5
```

---

### 2. Predict Task Completion (via Spring Boot)

**Endpoint:** `POST /api/ai/task-prediction/{taskId}`

**URL:** `http://localhost:8080/api/ai/task-prediction/22?employeeWorkload=5`

**Query Parameters:**
- `employeeWorkload` (optional, default: 0) - Current workload level

**Response:**
```json
{
  "taskId": 22,
  "prediction": "ON_TIME",
  "confidence": 0.8234
}
```

**cURL Example:**
```bash
curl -X POST "http://localhost:8080/api/ai/task-prediction/22?employeeWorkload=5"
```

---

### 3. Get Employee Clusters (via Spring Boot)

**Endpoint:** `GET /api/ai/employee-clusters`

**URL:** `http://localhost:8080/api/ai/employee-clusters`

**Response:**
```json
{
  "clusters": [
    {"employeeId": 1, "cluster": "High Performer"},
    {"employeeId": 2, "cluster": "Medium Performer"},
    {"employeeId": 3, "cluster": "Low Performer"}
  ]
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/ai/employee-clusters
```

---

### 4. Recommend Employee for Task (via Spring Boot)

**Endpoint:** `POST /api/ai/recommend-employee/{taskId}`

**URL:** `http://localhost:8080/api/ai/recommend-employee/22`

**What it does:**
1. Fetches task details from MySQL
2. Gets list of available employees
3. Calls AI service `/recommend-employee`
4. Returns recommendation

**Response:**
```json
{
  "taskId": 22,
  "recommendedEmployeeId": 5,
  "recommendationScore": 0.9234
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/ai/recommend-employee/22
```

---

### 5. Calculate Performance Scores (via Spring Boot)

**Endpoint:** `GET /api/ai/performance-scores`

**URL:** `http://localhost:8080/api/ai/performance-scores`

**Response:**
```json
{
  "scores": [
    {"employeeId": 1, "performanceScore": 78.33},
    {"employeeId": 2, "performanceScore": 45.67}
  ]
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/ai/performance-scores
```

---

### 6. Check AI Service Health (via Spring Boot)

**Endpoint:** `GET /api/ai/health`

**URL:** `http://localhost:8080/api/ai/health`

**Response (Healthy):**
```json
{
  "status": "healthy",
  "message": "AI service is available"
}
```

**Response (Unavailable):**
```json
{
  "status": "unavailable",
  "message": "AI service is not available"
}
```

**cURL Example:**
```bash
curl http://localhost:8080/api/ai/health
```

---

## ⚠️ Error Codes

| Code | Error | Cause |
|------|-------|-------|
| 200 | OK | Request successful |
| 400 | Bad Request | Invalid request format |
| 500 | Internal Server Error | Server-side error |
| 503 | Service Unavailable | AI service not running or models not trained |

---

## 🔍 Testing Quick Reference

```bash
# Test AI service health
curl http://localhost:8000/health

# Test Spring Boot health
curl http://localhost:8080/api/ai/health

# Test performance prediction
curl -X POST http://localhost:8000/predict-performance \
  -H "Content-Type: application/json" \
  -d '{"employee_id":1,"tasks_assigned":10,"tasks_completed":9,"missed_deadlines":1,"avg_completion_time":12.5}'

# Test via Spring Boot
curl -X POST http://localhost:8080/api/ai/performance/1

# Test task completion prediction
curl -X POST http://localhost:8000/predict-task-completion \
  -H "Content-Type: application/json" \
  -d '{"task_id":22,"difficulty":3,"deadline_days":10,"employee_workload":5}'

# Test employee clusters
curl -X POST http://localhost:8000/employee-clusters \
  -H "Content-Type: application/json" \
  -d '[{"id":1,"tasksCompleted":45,"missedDeadlines":2,"avgCompletionTime":8.5}]'

# Test recommendations
curl -X POST http://localhost:8000/recommend-employee \
  -H "Content-Type: application/json" \
  -d '{"task_id":22,"difficulty":3,"deadline_days":10,"available_employees":[1,2,5,7]}'

# Test performance score calculation
curl -X POST http://localhost:8000/calculate-performance-scores \
  -H "Content-Type: application/json" \
  -d '{"employees":[{"id":1,"tasksAssigned":10,"tasksCompleted":9,"missedDeadlines":1,"avgCompletionTime":12.5}]}'
```

---

**Version:** 1.0.0  
**Last Updated:** March 16, 2026
