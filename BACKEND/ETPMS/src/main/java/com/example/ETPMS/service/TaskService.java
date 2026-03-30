package com.example.ETPMS.service;

import com.example.ETPMS.dto.TaskDTO;
import com.example.ETPMS.model.Employee;
import com.example.ETPMS.model.Task;
import com.example.ETPMS.model.TaskStatus;
import com.example.ETPMS.repository.EmployeeRepository;
import com.example.ETPMS.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Task business logic.
 * Handles task assignment, status updates, and CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Get all tasks.
     */
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single task by ID.
     */
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        return toDTO(task);
    }

    /**
     * Get all tasks for a specific employee.
     */
    public List<TaskDTO> getTasksByEmployeeId(Long employeeId) {
        return taskRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new task and assign it to an employee.
     */
    public TaskDTO createTask(TaskDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        Task task = toEntity(dto, employee);
        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    /**
     * Update an existing task (title, description, deadline, status, assignee).
     */
    public TaskDTO updateTask(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDeadline(dto.getDeadline());
        task.setStatus(TaskStatus.valueOf(dto.getStatus()));
        task.setEmployee(employee);

        Task updated = taskRepository.save(task);
        return toDTO(updated);
    }

    /**
     * Delete a task.
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // ---- Mapping helpers ----

    private TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .status(task.getStatus().name())
                .employeeId(task.getEmployee().getId())
                .employeeName(task.getEmployee().getName())
                .build();
    }

    private Task toEntity(TaskDTO dto, Employee employee) {
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .deadline(dto.getDeadline())
                .status(dto.getStatus() != null ? TaskStatus.valueOf(dto.getStatus()) : TaskStatus.PENDING)
                .employee(employee)
                .build();
    }
}
