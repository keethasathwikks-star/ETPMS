package com.example.ETPMS.controller;

import com.example.ETPMS.dto.EmployeeDTO;
import com.example.ETPMS.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for Employee CRUD operations.
 * Base path: /api/employees
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private final EmployeeService employeeService;

    /** GET /api/employees — List all employees */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /** GET /api/employees/{id} — Get employee by ID */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /** POST /api/employees — Create a new employee */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO dto) {
        EmployeeDTO created = employeeService.createEmployee(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/employees/{id} — Update an existing employee */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    /** DELETE /api/employees/{id} — Delete an employee */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
