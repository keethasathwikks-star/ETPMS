"""
Automatic performance score calculation
"""
from typing import Dict, Any


class PerformanceCalculator:
    """Calculates performance score using predefined formula"""
    
    @staticmethod
    def calculate_performance_score(
        tasks_assigned: int,
        tasks_completed: int,
        missed_deadlines: int,
        avg_completion_time: float
    ) -> float:
        """
        Calculate automatic performance score.
        
        Formula:
        score = (tasks_completed / tasks_assigned) * 50 +
                (1 - missed_deadlines_ratio) * 30 +
                (speed_score) * 20
        
        Args:
            tasks_assigned: Total tasks assigned
            tasks_completed: Completed tasks count
            missed_deadlines: Number of missed deadlines
            avg_completion_time: Average completion time in days
            
        Returns:
            Performance score (0-100)
        """
        # Avoid division by zero
        completion_ratio = tasks_completed / max(tasks_assigned, 1)
        missed_ratio = missed_deadlines / max(tasks_assigned, 1)
        
        # Speed score: inverse of completion time (lower time = higher score)
        # Assuming 30 days is baseline
        speed_score = max(0, 1 - (avg_completion_time / 30.0))
        
        # Calculate final score
        score = (completion_ratio * 50) + ((1 - missed_ratio) * 30) + (speed_score * 20)
        
        # Clamp to 0-100
        return min(max(score, 0), 100)
    
    @staticmethod
    def calculate_batch_scores(employees_data: list) -> Dict[int, float]:
        """
        Calculate performance scores for multiple employees
        
        Args:
            employees_data: List of employee dicts
            
        Returns:
            Dict mapping employee ID to score
        """
        scores = {}
        for emp in employees_data:
            emp_id = emp.get('id')
            score = PerformanceCalculator.calculate_performance_score(
                tasks_assigned=emp.get('tasksAssigned', 0),
                tasks_completed=emp.get('tasksCompleted', 0),
                missed_deadlines=emp.get('missedDeadlines', 0),
                avg_completion_time=emp.get('avgCompletionTime', 0)
            )
            scores[emp_id] = score
        return scores
