# ETPMS AI Service - Setup & Integration Guide

This guide explains how to set up and run the AI microservice alongside your existing ETPMS backend.

## 📋 Overview

The AI service is a Python FastAPI microservice that provides machine learning predictions for:
- Employee performance scores
- Task completion predictions
- Employee performance clustering
- Employee recommendations for tasks
- Automatic performance score calculation

**Tech Stack:**
- Python 3.8+
- FastAPI
- scikit-learn
- pandas
- numpy
- joblib

---

## 🚀 Quick Start

### Step 1: Install Python Dependencies

```bash
cd c:\TTPROJECT\ETPMS\ai-service
pip install -r requirements.txt
```

### Step 2: Train Machine Learning Models

Before running the AI service, train the models:

```bash
python train_models.py
```

**Expected Output:**
```
============================================================
ETPMS AI SERVICE: MODEL TRAINING
============================================================

Generating sample training data...
✓ Generated synthetic data for 100 employees

Training models...

[1/5] Training Performance Predictor (Linear Regression)...
✓ Performance Predictor trained | R² Score: 0.8234

[2/5] Training Task Completion Predictor (Logistic Regression)...
✓ Task Completion Predictor trained | Accuracy: 0.7100

[3/5] Training Employee Clusterer (K-Means)...
✓ Employee Clusterer trained | Inertia: 45.5432

[4/5] Training Employee Recommender (Nearest Neighbors)...
✓ Employee Recommender trained | K-Neighbors: 5

[5/5] Saving training metadata...
✓ Metadata saved

============================================================
✓ ALL MODELS TRAINED SUCCESSFULLY
✓ Models saved to: c:\TTPROJECT\ETPMS\ai-service\models
============================================================
```

### Step 3: Start the AI Service

```bash
cd c:\TTPROJECT\ETPMS\ai-service
python -m uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```

The service will be available at: **http://localhost:8000**

### Step 4: Verify AI Service is Running

```bash
curl http://localhost:8000/health
```

Expected response:
```json
{
  "status": "healthy",
  "models_available": true,
  "message": "All models loaded"
}
```

### Step 5: Start Spring Boot Backend

The backend is already configured to call the AI service at `http://localhost:8000`.

```bash
cd c:\TTPROJECT\ETPMS\BACKEND\ETPMS
.\mvnw spring-boot:run
```

---

## 🌐 API Endpoints

### 1. Health Check

```bash
GET http://localhost:8000/health
```

Check if AI service is running and models are loaded.

**Response:**
```json
{
  "status": "healthy",
  "models_available": true,
  "message": "All models loaded"
}
```

---

### 2. Predict Employee Performance

```bash
POST http://localhost:8000/predict-performance
Content-Type: application/json

{
  "employee_id": 5,
  "tasks_assigned": 10,
  "tasks_completed": 9,
  "missed_deadlines": 1,
  "avg_completion_time": 12.5
}
```

**Response:**
```json
{
  "employee_id": 5,
  "predicted_score": 88.45
}
```

---

### 3. Predict Task Completion

```bash
POST http://localhost:8000/predict-task-completion
Content-Type: application/json

{
  "task_id": 22,
  "difficulty": 3,
  "deadline_days": 10,
  "employee_workload": 5
}
```

**Response:**
```json
{
  "task_id": 22,
  "prediction": "ON_TIME",
  "confidence": 0.8234
}
```

---

### 4. Get Employee Clusters

```bash
POST http://localhost:8000/employee-clusters
Content-Type: application/json

[
  {
    "id": 1,
    "tasksCompleted": 45,
    "missedDeadlines": 2,
    "avgCompletionTime": 8.5
  },
  {
    "id": 2,
    "tasksCompleted": 15,
    "missedDeadlines": 8,
    "avgCompletionTime": 20.0
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
      "cluster": "Low Performer"
    }
  ]
}
```

---

### 5. Recommend Employee for Task

```bash
POST http://localhost:8000/recommend-employee
Content-Type: application/json

{
  "task_id": 22,
  "difficulty": 3,
  "deadline_days": 10,
  "available_employees": [1, 2, 5, 7]
}
```

**Response:**
```json
{
  "task_id": 22,
  "recommended_employee_id": 5,
  "recommendation_score": 0.9234
}
```

---

### 6. Calculate Performance Scores

```bash
POST http://localhost:8000/calculate-performance-scores
Content-Type: application/json

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

---

## 🔗 Spring Boot Integration

### Backend Endpoints

The Spring Boot backend exposes these endpoints that call the AI service:

#### 1. Predict Employee Performance
```bash
POST /api/ai/performance/{employeeId}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/ai/performance/5
```

**Response:**
```json
{
  "employeeId": 5,
  "predictedScore": 88.45
}
```

---

#### 2. Predict Task Completion
```bash
POST /api/ai/task-prediction/{taskId}?employeeWorkload=5
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/ai/task-prediction/22?employeeWorkload=5
```

**Response:**
```json
{
  "taskId": 22,
  "prediction": "ON_TIME",
  "confidence": 0.8234
}
```

---

#### 3. Get Employee Clusters
```bash
GET /api/ai/employee-clusters
```

**Example:**
```bash
curl http://localhost:8080/api/ai/employee-clusters
```

**Response:**
```json
{
  "clusters": [
    {
      "employeeId": 1,
      "cluster": "High Performer"
    },
    {
      "employeeId": 2,
      "cluster": "Medium Performer"
    }
  ]
}
```

---

#### 4. Recommend Employee
```bash
POST /api/ai/recommend-employee/{taskId}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/ai/recommend-employee/22
```

**Response:**
```json
{
  "taskId": 22,
  "recommendedEmployeeId": 5,
  "recommendationScore": 0.9234
}
```

---

#### 5. Calculate Performance Scores
```bash
GET /api/ai/performance-scores
```

**Example:**
```bash
curl http://localhost:8080/api/ai/performance-scores
```

**Response:**
```json
{
  "scores": [
    {
      "employeeId": 1,
      "performanceScore": 78.33
    },
    {
      "employeeId": 2,
      "performanceScore": 45.67
    }
  ]
}
```

---

#### 6. AI Service Health Check
```bash
GET /api/ai/health
```

**Response:**
```json
{
  "status": "healthy",
  "message": "AI service is available"
}
```

---

## 📁 Project Structure

```
c:\TTPROJECT\ETPMS\
├── ai-service/
│   ├── models/                    # Trained ML models (generated by train_models.py)
│   │   ├── performance_predictor.pkl
│   │   ├── performance_scaler.pkl
│   │   ├── task_predictor.pkl
│   │   ├── task_scaler.pkl
│   │   ├── employee_clusterer.pkl
│   │   ├── clustering_scaler.pkl
│   │   ├── employee_recommender.pkl
│   │   ├── recommender_scaler.pkl
│   │   └── metadata.json
│   ├── utils/                     # Utility modules
│   │   ├── __init__.py
│   │   ├── data_loader.py
│   │   └── performance_calculator.py
│   ├── main.py                    # FastAPI application
│   ├── train_models.py            # Model training script
│   └── requirements.txt           # Python dependencies
│
├── BACKEND/ETPMS/
│   ├── src/main/java/com/example/ETPMS/
│   │   ├── config/
│   │   │   ├── CorsConfig.java
│   │   │   ├── SecurityConfig.java
│   │   │   └── RestClientConfig.java         # NEW
│   │   ├── controller/
│   │   │   ├── DashboardController.java
│   │   │   ├── EmployeeController.java
│   │   │   ├── PerformanceReviewController.java
│   │   │   ├── TaskController.java
│   │   │   └── AiAnalyticsController.java    # NEW
│   │   ├── dto/
│   │   │   ├── ...
│   │   │   └── AIResponseDTOs.java           # NEW
│   │   ├── service/
│   │   │   ├── DashboardService.java
│   │   │   ├── EmployeeService.java
│   │   │   ├── PerformanceReviewService.java
│   │   │   ├── TaskService.java
│   │   │   └── AiAnalyticsService.java       # NEW
│   │   └── ...
│   ├── src/main/resources/
│   │   └── application.properties             # MODIFIED (added ai.service.url)
│   └── pom.xml
│
└── FRONTEND/eptms/
    └── ... (no changes needed)
```

---

## 🔧 Configuration

### AI Service URL

The AI service URL is configurable in `application.properties`:

```properties
# Default (localhost)
ai.service.url=http://localhost:8000

# Production (if deployed elsewhere)
ai.service.url=http://your-ai-service-host:8000
```

---

## 🧠 Machine Learning Models

### 1. Performance Predictor (Linear Regression)
- **Input:** tasks_assigned, tasks_completed, missed_deadlines, avg_completion_time
- **Output:** Predicted performance score (0-100)
- **Model:** sklearn.linear_model.LinearRegression

### 2. Task Completion Predictor (Logistic Regression)
- **Input:** difficulty, deadline_days, employee_workload
- **Output:** Prediction (ON_TIME=1 or LATE=0) with confidence
- **Model:** sklearn.linear_model.LogisticRegression

### 3. Employee Clusterer (K-Means)
- **Input:** tasks_completed, missed_deadlines, avg_completion_time
- **Output:** Cluster assignment (High/Medium/Low Performer)
- **Model:** sklearn.cluster.KMeans (n_clusters=3)

### 4. Employee Recommender (Nearest Neighbors)
- **Input:** tasks_completed, missed_deadlines, avg_completion_time
- **Output:** Recommended employee ID with score
- **Model:** sklearn.neighbors.NearestNeighbors (n_neighbors=5)

### 5. Performance Calculator (Custom Formula)
- **Formula:** (completion_ratio * 50) + ((1 - missed_ratio) * 30) + (speed_score * 20)
- **Output:** Automatic performance score (0-100)

---

## 📊 Data Flow

```
React Frontend
    ↓
Spring Boot Backend (Port 8080)
    ├── Fetches employee/task data from MySQL
    ├── Sends request to AI Service
    ↓
Python AI Service (Port 8000)
    ├── Loads trained ML models
    ├── Processes input features
    ├── Runs predictions
    ↓ (returns predictions)
Spring Boot Backend
    ├── Receives predictions
    ↓
React Frontend (displays results in dashboard)
```

---

## 🛠️ Troubleshooting

### Issue: "AI service unavailable"

**Solution:**
1. Ensure AI service is running: `python -m uvicorn main:app --host 0.0.0.0 --port 8000`
2. Check if port 8000 is already in use: `netstat -ano | findstr :8000`
3. Verify models are trained: Check if `models/` folder contains `.pkl` files

### Issue: "Model not found"

**Solution:**
1. Train models: `python train_models.py`
2. Verify `models/` folder exists and contains all `.pkl` files
3. Check file permissions

### Issue: Import errors in Python

**Solution:**
1. Install dependencies: `pip install -r requirements.txt`
2. Use Python 3.8+: `python --version`
3. Create a virtual environment:
   ```bash
   python -m venv venv
   # Windows:
   venv\Scripts\activate
   # Then install dependencies
   pip install -r requirements.txt
   ```

### Issue: CORS errors from frontend

**Solution:**
- CORS is enabled in FastAPI: `allow_origins=["*"]`
- If issues persist, check backend logs: Spring Boot RestTemplate should be calling the correct URL

---

## 📝 Example Usage Flow

1. **Start all services:**
   ```bash
   # Terminal 1: Train models
   cd c:\TTPROJECT\ETPMS\ai-service
   python train_models.py
   
   # Terminal 2: Start AI service
   cd c:\TTPROJECT\ETPMS\ai-service
   python -m uvicorn main:app --host 0.0.0.0 --port 8000
   
   # Terminal 3: Start backend
   cd c:\TTPROJECT\ETPMS\BACKEND\ETPMS
   ./mvnw spring-boot:run
   
   # Terminal 4: Start frontend
   cd c:\TTPROJECT\ETPMS\FRONTEND\eptms
   npm start
   ```

2. **Verify health:**
   ```bash
   # Check AI service
   curl http://localhost:8000/health
   
   # Check Spring Boot
   curl http://localhost:8080/api/ai/health
   ```

3. **Test employee performance prediction:**
   ```bash
   curl -X POST http://localhost:8080/api/ai/performance/1
   ```

4. **View results in React dashboard:**
   - Navigate to http://localhost:3000
   - New AI Analytics widgets will appear once data is available

---

## 🚀 Production Deployment

### Docker Deployment

Create `ai-service/Dockerfile`:
```dockerfile
FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
```

Build and run:
```bash
docker build -t etpms-ai-service .
docker run -p 8000:8000 etpms-ai-service
```

---

## 📚 References

- [FastAPI Documentation](https://fastapi.tiangolo.com/)
- [scikit-learn Documentation](https://scikit-learn.org/)
- [Spring Boot RestTemplate](https://spring.io/guides/gs/consuming-rest/)

---

## ✅ Checklist

- [ ] Python 3.8+ installed
- [ ] Dependencies installed: `pip install -r requirements.txt`
- [ ] Models trained: `python train_models.py`
- [ ] AI service running: `python -m uvicorn main:app --host 0.0.0.0 --port 8000`
- [ ] Spring Boot backend running: `./mvnw spring-boot:run`
- [ ] Frontend running: `npm start`
- [ ] Health check passing: `curl http://localhost:8000/health`
- [ ] API endpoints responding: Test `/api/ai/health`

---

**Created:** March 16, 2026  
**AI Service Version:** 1.0.0  
**Last Updated:** March 16, 2026
