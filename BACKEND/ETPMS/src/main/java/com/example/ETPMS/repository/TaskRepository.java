package com.example.ETPMS.repository;

import com.example.ETPMS.model.Task;
import com.example.ETPMS.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Task entity CRUD operations.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find all tasks assigned to a specific employee
    List<Task> findByEmployeeId(Long employeeId);

    // Count tasks by their status (PENDING, IN_PROGRESS, COMPLETED)
    long countByStatus(TaskStatus status);
}
