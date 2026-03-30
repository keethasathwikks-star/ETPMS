"""
Model training script for AI service
Trains and saves all ML models using joblib
"""
import os
import json
import numpy as np
import pandas as pd
import joblib
from sklearn.linear_model import LinearRegression, LogisticRegression
from sklearn.cluster import KMeans
from sklearn.neighbors import NearestNeighbors
from sklearn.preprocessing import StandardScaler
from datetime import datetime
from typing import Dict, Any


class ModelTrainer:
    """Trains and saves machine learning models"""
    
    def __init__(self, models_dir: str = "models"):
        self.models_dir = models_dir
        os.makedirs(models_dir, exist_ok=True)
        self.scalers = {}
    
    def generate_sample_data(self) -> Dict[str, Any]:
        """Generate synthetic training data for demonstration"""
        np.random.seed(42)
        n_samples = 100
        
        # Employee performance data
        employees = {
            'employee_id': np.arange(1, n_samples + 1),
            'tasks_assigned': np.random.randint(5, 50, n_samples),
            'tasks_completed': np.random.randint(3, 45, n_samples),
            'missed_deadlines': np.random.randint(0, 15, n_samples),
            'avg_completion_time': np.random.uniform(5, 30, n_samples)
        }
        
        # Task completion data
        tasks = {
            'task_id': np.arange(101, 101 + n_samples),
            'difficulty': np.random.randint(1, 6, n_samples),
            'deadline_days': np.random.randint(5, 60, n_samples),
            'employee_workload': np.random.randint(0, 20, n_samples),
            # On-time = 1, Late = 0
            'completed_on_time': np.random.binomial(1, 0.7, n_samples)
        }
        
        return {'employees': employees, 'tasks': tasks}
    
    def train_performance_predictor(self, data: Dict[str, Any]) -> None:
        """Train Linear Regression model for employee performance prediction"""
        print("[1/5] Training Performance Predictor (Linear Regression)...")
        
        emp_data = data['employees']
        X = np.column_stack([
            emp_data['tasks_assigned'],
            emp_data['tasks_completed'],
            emp_data['missed_deadlines'],
            emp_data['avg_completion_time']
        ])
        
        # Target: Performance score (0-100)
        # Formula: (tasks_completed / tasks_assigned) * 50 + (1 - missed_ratio) * 30 + speed * 20
        y = np.array([
            (emp_data['tasks_completed'][i] / max(emp_data['tasks_assigned'][i], 1)) * 50 +
            (1 - emp_data['missed_deadlines'][i] / max(emp_data['tasks_assigned'][i], 1)) * 30 +
            (1 - min(emp_data['avg_completion_time'][i] / 30, 1)) * 20
            for i in range(len(X))
        ])
        
        # Normalize features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        model = LinearRegression()
        model.fit(X_scaled, y)
        
        # Save model and scaler
        joblib.dump(model, os.path.join(self.models_dir, 'performance_predictor.pkl'))
        joblib.dump(scaler, os.path.join(self.models_dir, 'performance_scaler.pkl'))
        self.scalers['performance'] = scaler
        
        print(f"✓ Performance Predictor trained | R² Score: {model.score(X_scaled, y):.4f}")
    
    def train_task_completion_predictor(self, data: Dict[str, Any]) -> None:
        """Train Logistic Regression model for task completion prediction"""
        print("[2/5] Training Task Completion Predictor (Logistic Regression)...")
        
        task_data = data['tasks']
        X = np.column_stack([
            task_data['difficulty'],
            task_data['deadline_days'],
            task_data['employee_workload']
        ])
        y = task_data['completed_on_time']  # 1 = on-time, 0 = late
        
        # Normalize features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        model = LogisticRegression(random_state=42, max_iter=1000)
        model.fit(X_scaled, y)
        
        # Save model and scaler
        joblib.dump(model, os.path.join(self.models_dir, 'task_predictor.pkl'))
        joblib.dump(scaler, os.path.join(self.models_dir, 'task_scaler.pkl'))
        self.scalers['task'] = scaler
        
        accuracy = model.score(X_scaled, y)
        print(f"✓ Task Completion Predictor trained | Accuracy: {accuracy:.4f}")
    
    def train_employee_clusterer(self, data: Dict[str, Any]) -> None:
        """Train K-Means clustering model for employee segmentation"""
        print("[3/5] Training Employee Clusterer (K-Means)...")
        
        emp_data = data['employees']
        X = np.column_stack([
            emp_data['tasks_completed'],
            emp_data['missed_deadlines'],
            emp_data['avg_completion_time']
        ])
        
        # Normalize features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        # 3 clusters: Low, Medium, High performers
        model = KMeans(n_clusters=3, random_state=42, n_init=10)
        model.fit(X_scaled)
        
        # Save model and scaler
        joblib.dump(model, os.path.join(self.models_dir, 'employee_clusterer.pkl'))
        joblib.dump(scaler, os.path.join(self.models_dir, 'clustering_scaler.pkl'))
        self.scalers['clustering'] = scaler
        
        print(f"✓ Employee Clusterer trained | Inertia: {model.inertia_:.4f}")
    
    def train_employee_recommender(self, data: Dict[str, Any]) -> None:
        """Train Nearest Neighbors model for employee recommendation"""
        print("[4/5] Training Employee Recommender (Nearest Neighbors)...")
        
        emp_data = data['employees']
        X = np.column_stack([
            emp_data['tasks_completed'],
            emp_data['missed_deadlines'],
            emp_data['avg_completion_time']
        ])
        
        # Normalize features
        scaler = StandardScaler()
        X_scaled = scaler.fit_transform(X)
        
        # Use 5 neighbors
        model = NearestNeighbors(n_neighbors=min(5, len(X_scaled)), algorithm='auto')
        model.fit(X_scaled)
        
        # Save model and scaler
        joblib.dump(model, os.path.join(self.models_dir, 'employee_recommender.pkl'))
        joblib.dump(scaler, os.path.join(self.models_dir, 'recommender_scaler.pkl'))
        self.scalers['recommender'] = scaler
        
        print(f"✓ Employee Recommender trained | K-Neighbors: 5")
    
    def save_training_metadata(self) -> None:
        """Save training metadata and timestamp"""
        print("[5/5] Saving training metadata...")
        
        metadata = {
            'timestamp': datetime.now().isoformat(),
            'models': [
                'performance_predictor.pkl',
                'task_predictor.pkl',
                'employee_clusterer.pkl',
                'employee_recommender.pkl'
            ],
            'scalers': [
                'performance_scaler.pkl',
                'task_scaler.pkl',
                'clustering_scaler.pkl',
                'recommender_scaler.pkl'
            ],
            'version': '1.0'
        }
        
        with open(os.path.join(self.models_dir, 'metadata.json'), 'w') as f:
            json.dump(metadata, f, indent=2)
        
        print(f"✓ Metadata saved")
    
    def train_all_models(self) -> None:
        """Train all models and save them"""
        print("=" * 60)
        print("ETPMS AI SERVICE: MODEL TRAINING")
        print("=" * 60)
        
        # Generate sample data
        print("\nGenerating sample training data...")
        data = self.generate_sample_data()
        print(f"✓ Generated synthetic data for {len(data['employees']['employee_id'])} employees")
        
        # Train all models
        print("\nTraining models...\n")
        self.train_performance_predictor(data)
        self.train_task_completion_predictor(data)
        self.train_employee_clusterer(data)
        self.train_employee_recommender(data)
        self.save_training_metadata()
        
        print("\n" + "=" * 60)
        print("✓ ALL MODELS TRAINED SUCCESSFULLY")
        print(f"✓ Models saved to: {os.path.abspath(self.models_dir)}")
        print("=" * 60)


if __name__ == "__main__":
    trainer = ModelTrainer()
    trainer.train_all_models()
