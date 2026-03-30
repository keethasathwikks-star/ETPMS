package com.example.ETPMS.repository;

import com.example.ETPMS.model.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for PerformanceReview entity CRUD operations.
 */
@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    // Find all reviews for a specific employee
    List<PerformanceReview> findByEmployeeId(Long employeeId);

    // Calculate the average rating across all reviews
    @Query("SELECT COALESCE(AVG(pr.rating), 0) FROM PerformanceReview pr")
    double findAverageRating();
}
