"""
ETPMS AI Service - Main FastAPI Application
Provides ML-powered analytics for the ETPMS system
"""
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Dict, Any, Optional
import joblib
import os
import numpy as np
import uvicorn
from pathlib import Path
from utils import DataLoader, PerformanceCalculator

# ============================================================================
# FastAPI App Setup
# ============================================================================
app = FastAPI(title="ETPMS AI Service", version="1.0.0")

# Enable CORS for Spring Boot backend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ============================================================================
# Models Directory
# ============================================================================
MODELS_DIR = Path(__file__).parent / "models"

# ============================================================================
# Request/Response Models
# ============================================================================
class PerformancePredictionRequest(BaseModel):
    employee_id: int
    tasks_assigned: int
    tasks_completed: int
    missed_deadlines: int
    avg_completion_time: float


class PerformancePredictionResponse(BaseModel):
    employee_id: int
    predicted_score: float


class TaskCompletionRequest(BaseModel):
    task_id: int
    difficulty: int
    deadline_days: int
    employee_workload: int


class TaskCompletionResponse(BaseModel):
    task_id: int
    prediction: str  # "ON_TIME" or "LATE"
    confidence: float


class EmployeeCluster(BaseModel):
    employee_id: int
    cluster: str  # "High Performer", "Medium Performer", "Low Performer"


class EmployeeClusterResponse(BaseModel):
    clusters: List[EmployeeCluster]


class TaskRecommendationRequest(BaseModel):
    task_id: int
    difficulty: int
    deadline_days: int
    available_employees: List[int]


class TaskRecommendationResponse(BaseModel):
    task_id: int
    recommended_employee_id: int
    recommendation_score: float


class AutoPerformanceScoreRequest(BaseModel):
    employees: List[Dict[str, Any]]


class AutoPerformanceScoreItem(BaseModel):
    employee_id: int
    performance_score: float


class AutoPerformanceScoreResponse(BaseModel):
    scores: List[AutoPerformanceScoreItem]


# ============================================================================
# Model Loaders
# ============================================================================
# ============================================================================
# Global Model Cache
# ============================================================================
MODELS = {}

@app.on_event("startup")
async def startup_event():
    """Load all models into memory at startup"""
    try:
        model_names = [
            'performance_predictor', 'task_predictor', 
            'employee_clusterer', 'employee_recommender',
            'performance_scaler', 'task_scaler', 
            'clustering_scaler', 'recommender_scaler'
        ]
        
        for name in model_names:
            path = MODELS_DIR / f"{name}.pkl"
            if path.exists():
                MODELS[name] = joblib.load(path)
                print(f"Loaded model: {name}")
            else:
                print(f"Warning: Model file {path} not found.")
    except Exception as e:
        print(f"Error loading models during startup: {e}")


def get_model(name: str):
    """Retrieve a model from cache or raise error if missing"""
    if name not in MODELS:
        # Fallback to loading on demand if not in cache (e.g. if startup failed)
        path = MODELS_DIR / f"{name}.pkl"
        if not path.exists():
             raise FileNotFoundError(f"Model '{name}' not found. Run train_models.py first.")
        MODELS[name] = joblib.load(path)
    return MODELS[name]


def load_model(model_name: str):
    """Load a trained model from disk (kept for backward compatibility)"""
    return get_model(model_name)


def load_scaler(scaler_name: str):
    """Load a trained scaler from disk (kept for backward compatibility)"""
    return get_model(scaler_name)


# ============================================================================
# Health Check
# ============================================================================
@app.get("/health")
async def health_check():
    """Health check endpoint"""
    models_exist = all([
        (MODELS_DIR / f"{m}.pkl").exists()
        for m in ['performance_predictor', 'task_predictor', 'employee_clusterer', 'employee_recommender']
    ])
    
    return {
        "status": "healthy" if models_exist else "models_not_trained",
        "models_available": models_exist,
        "message": "Run 'python train_models.py' to train models" if not models_exist else "All models loaded"
    }


# ============================================================================
# 1. EMPLOYEE PERFORMANCE PREDICTION
# ============================================================================
@app.post("/predict-performance", response_model=PerformancePredictionResponse)
async def predict_performance(request: PerformancePredictionRequest):
    """
    Predict employee performance score using Linear Regression
    
    Features:
    - tasks_assigned: Total tasks assigned to employee
    - tasks_completed: Completed tasks count
    - missed_deadlines: Number of missed deadlines
    - avg_completion_time: Average completion time in days
    
    Returns prediction score (0-100)
    """
    try:
        model = load_model("performance_predictor")
        scaler = load_scaler("performance_scaler")
        
        # Prepare features
        features = np.array([[
            request.tasks_assigned,
            request.tasks_completed,
            request.missed_deadlines,
            request.avg_completion_time
        ]])
        
        # Scale features
        features_scaled = scaler.transform(features)
        
        # Predict
        predicted_score = float(model.predict(features_scaled)[0])
        
        # Clamp to 0-100
        predicted_score = max(0, min(100, predicted_score))
        
        return PerformancePredictionResponse(
            employee_id=request.employee_id,
            predicted_score=round(predicted_score, 2)
        )
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {str(e)}")


# ============================================================================
# 2. TASK COMPLETION PREDICTION
# ============================================================================
@app.post("/predict-task-completion", response_model=TaskCompletionResponse)
async def predict_task_completion(request: TaskCompletionRequest):
    """
    Predict whether a task will be completed on time using Logistic Regression
    
    Features:
    - difficulty: Task difficulty (1-5 scale)
    - deadline_days: Days until deadline
    - employee_workload: Current workload of assigned employee
    
    Returns "ON_TIME" or "LATE" with confidence score
    """
    try:
        model = load_model("task_predictor")
        scaler = load_scaler("task_scaler")
        
        # Prepare features
        features = np.array([[
            request.difficulty,
            request.deadline_days,
            request.employee_workload
        ]])
        
        # Scale features
        features_scaled = scaler.transform(features)
        
        # Predict (1 = on-time, 0 = late)
        prediction = int(model.predict(features_scaled)[0])
        confidence = float(model.predict_proba(features_scaled)[0].max())
        
        return TaskCompletionResponse(
            task_id=request.task_id,
            prediction="ON_TIME" if prediction == 1 else "LATE",
            confidence=round(confidence, 4)
        )
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Prediction error: {str(e)}")


# ============================================================================
# 3. EMPLOYEE CLUSTERING
# ============================================================================
@app.post("/employee-clusters", response_model=EmployeeClusterResponse)
async def get_employee_clusters(employees: List[Dict[str, Any]]):
    """
    Cluster employees into performance tiers using K-Means
    
    Features used:
    - tasks_completed: Number of completed tasks
    - missed_deadlines: Number of missed deadlines
    - avg_completion_time: Average completion time in days
    
    Returns clusters: High Performer, Medium Performer, Low Performer
    """
    try:
        if not employees:
            return EmployeeClusterResponse(clusters=[])
        
        model = load_model("employee_clusterer")
        scaler = load_scaler("clustering_scaler")
        
        # Prepare features for all employees
        features = np.array([
            [
                emp.get('tasksCompleted', 0),
                emp.get('missedDeadlines', 0),
                emp.get('avgCompletionTime', 0)
            ]
            for emp in employees
        ])
        
        # Scale features
        features_scaled = scaler.transform(features)
        
        # Predict clusters
        cluster_labels = model.predict(features_scaled)
        
        # Get cluster centers and determine which is which
        centers = scaler.inverse_transform(model.cluster_centers_)
        
        # Determine cluster meanings based on task completion and missed deadlines
        cluster_performance = {}
        for i, center in enumerate(centers):
            # Higher completed tasks and lower missed deadlines = higher performer
            score = center[0] - center[1]  # tasks_completed - missed_deadlines
            cluster_performance[i] = score
        
        # Sort clusters by performance
        sorted_clusters = sorted(cluster_performance.items(), key=lambda x: x[1], reverse=True)
        cluster_names = {
            sorted_clusters[0][0]: "High Performer",
            sorted_clusters[1][0]: "Medium Performer",
            sorted_clusters[2][0]: "Low Performer"
        }
        
        # Build response
        result = []
        for emp, cluster_id in zip(employees, cluster_labels):
            result.append(EmployeeCluster(
                employee_id=emp['id'],
                cluster=cluster_names.get(cluster_id, "Unknown")
            ))
        
        return EmployeeClusterResponse(clusters=result)
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Clustering error: {str(e)}")


# ============================================================================
# 4. TASK RECOMMENDATION ENGINE
# ============================================================================
@app.post("/recommend-employee", response_model=TaskRecommendationResponse)
async def recommend_employee(request: TaskRecommendationRequest):
    """
    Recommend the best employee for a task using Nearest Neighbors
    
    Considers:
    - Employee history of task completion
    - Completion speed
    - Missed deadlines
    
    Returns recommended employee with recommendation score
    """
    try:
        if not request.available_employees:
            raise HTTPException(status_code=400, detail="No available employees")
        
        model = load_model("employee_recommender")
        scaler = load_scaler("recommender_scaler")
        
        # Task features (we're looking for similar employees)
        task_features = np.array([[
            request.difficulty,          # Use difficulty as a proxy for required skill
            request.deadline_days,       # Time pressure
            0                            # Normalized workload
        ]])
        
        # Scale task features
        task_features_scaled = scaler.transform(task_features)
        
        # Find nearest neighbors among available employees
        distances, indices = model.kneighbors(task_features_scaled)
        
        # Get the closest match
        closest_idx = indices[0][0]
        closest_distance = distances[0][0]
        
        # Convert distance to recommendation score (inverse relationship)
        # Closer = better recommendation (higher score)
        recommendation_score = max(0, 1 - closest_distance)
        
        # For simplicity, return the first available employee (in production, 
        # you'd map indices to actual employee IDs from your database)
        recommended_id = request.available_employees[closest_idx % len(request.available_employees)]
        
        return TaskRecommendationResponse(
            task_id=request.task_id,
            recommended_employee_id=recommended_id,
            recommendation_score=round(recommendation_score, 4)
        )
    except FileNotFoundError as e:
        raise HTTPException(status_code=503, detail=str(e))
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Recommendation error: {str(e)}")


# ============================================================================
# 5. AUTOMATIC PERFORMANCE SCORE
# ============================================================================
@app.post("/calculate-performance-scores", response_model=AutoPerformanceScoreResponse)
async def calculate_performance_scores(request: AutoPerformanceScoreRequest):
    """
    Calculate automatic performance scores for employees using formula
    
    Formula:
    score = (tasks_completed / tasks_assigned) * 50 +
            (1 - missed_deadlines_ratio) * 30 +
            (speed_score) * 20
    
    Returns performance scores for all employees
    """
    try:
        scores = []
        for emp in request.employees:
            score = PerformanceCalculator.calculate_performance_score(
                tasks_assigned=emp.get('tasksAssigned', 0),
                tasks_completed=emp.get('tasksCompleted', 0),
                missed_deadlines=emp.get('missedDeadlines', 0),
                avg_completion_time=emp.get('avgCompletionTime', 0)
            )
            
            scores.append(AutoPerformanceScoreItem(
                employee_id=emp['id'],
                performance_score=round(score, 2)
            ))
        
        return AutoPerformanceScoreResponse(scores=scores)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Calculation error: {str(e)}")


# ============================================================================
# Batch Predictions
# ============================================================================
@app.post("/batch-predict-performance")
async def batch_predict_performance(requests: List[PerformancePredictionRequest]):
    """Predict performance for multiple employees at once"""
    results = []
    for req in requests:
        result = await predict_performance(req)
        results.append(result)
    return results


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
