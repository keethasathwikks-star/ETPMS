"""
Data loading utilities for AI service
Formats data from Spring Boot API responses
"""
import pandas as pd
import numpy as np
from typing import List, Dict, Any


class DataLoader:
    """Loads and preprocesses employee and task data"""
    
    @staticmethod
    def prepare_employee_data(employees_data: List[Dict[str, Any]]) -> pd.DataFrame:
        """
        Convert employee list to DataFrame with computed features
        
        Args:
            employees_data: List of employee dicts from Spring Boot API
            
        Returns:
            DataFrame with employee features
        """
        if not employees_data:
            return pd.DataFrame()
        
        df = pd.DataFrame(employees_data)
        
        # Ensure required columns exist with defaults
        if 'tasksAssigned' not in df.columns:
            df['tasksAssigned'] = 0
        if 'tasksCompleted' not in df.columns:
            df['tasksCompleted'] = 0
        if 'missedDeadlines' not in df.columns:
            df['missedDeadlines'] = 0
        if 'avgCompletionTime' not in df.columns:
            df['avgCompletionTime'] = 0
        
        return df
    
    @staticmethod
    def prepare_task_data(task: Dict[str, Any]) -> pd.DataFrame:
        """
        Convert single task to feature vector
        
        Args:
            task: Task dict from Spring Boot API
            
        Returns:
            DataFrame with single row for prediction
        """
        features = {
            'difficulty': [task.get('difficulty', 1)],
            'deadline_days': [task.get('deadlineDays', 30)],
            'employee_workload': [task.get('employeeWorkload', 0)]
        }
        return pd.DataFrame(features)
    
    @staticmethod
    def normalize_features(X: np.ndarray) -> np.ndarray:
        """Normalize features to 0-1 range"""
        return (X - X.min(axis=0)) / (X.max(axis=0) - X.min(axis=0) + 1e-8)
