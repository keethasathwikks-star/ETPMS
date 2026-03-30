package com.example.ETPMS.service;

import com.example.ETPMS.dto.EmployeeDTO;
import com.example.ETPMS.model.Employee;
import com.example.ETPMS.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Employee business logic.
 * Handles DTO ↔ Entity conversion and CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Get all employees.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single employee by ID.
     */
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return toDTO(employee);
    }

    /**
     * Create a new employee.
     */
    public EmployeeDTO createEmployee(EmployeeDTO dto) {
        Employee employee = toEntity(dto);
        Employee saved = employeeRepository.save(employee);
        return toDTO(saved);
    }

    /**
     * Update an existing employee.
     */
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(dto.getRole());
        Employee updated = employeeRepository.save(employee);
        return toDTO(updated);
    }

    /**
     * Delete an employee.
     */
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    // ---- Mapping helpers ----

    private EmployeeDTO toDTO(Employee employee) {
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartment())
                .role(employee.getRole())
                .build();
    }

    private Employee toEntity(EmployeeDTO dto) {
        return Employee.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .department(dto.getDepartment())
                .role(dto.getRole())
                .build();
    }
}
