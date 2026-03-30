package com.example.ETPMS.repository;

import com.example.ETPMS.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Employee entity CRUD operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Check if an employee with the given email already exists
    boolean existsByEmail(String email);
}
