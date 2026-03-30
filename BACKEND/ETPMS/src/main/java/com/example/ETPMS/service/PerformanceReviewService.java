package com.example.ETPMS.service;

import com.example.ETPMS.dto.PerformanceReviewDTO;
import com.example.ETPMS.model.Employee;
import com.example.ETPMS.model.PerformanceReview;
import com.example.ETPMS.repository.EmployeeRepository;
import com.example.ETPMS.repository.PerformanceReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for PerformanceReview business logic.
 * Handles creating and retrieving performance reviews.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PerformanceReviewService {

    private final PerformanceReviewRepository reviewRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Get all performance reviews.
     */
    public List<PerformanceReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single review by ID.
     */
    public PerformanceReviewDTO getReviewById(Long id) {
        PerformanceReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        return toDTO(review);
    }

    /**
     * Get all reviews for a specific employee.
     */
    public List<PerformanceReviewDTO> getReviewsByEmployeeId(Long employeeId) {
        return reviewRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new performance review.
     */
    public PerformanceReviewDTO createReview(PerformanceReviewDTO dto) {
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));
        PerformanceReview review = toEntity(dto, employee);
        PerformanceReview saved = reviewRepository.save(review);
        return toDTO(saved);
    }

    /**
     * Update an existing review.
     */
    public PerformanceReviewDTO updateReview(Long id, PerformanceReviewDTO dto) {
        PerformanceReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + id));
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + dto.getEmployeeId()));

        review.setRating(dto.getRating());
        review.setFeedback(dto.getFeedback());
        review.setReviewDate(dto.getReviewDate());
        review.setEmployee(employee);

        PerformanceReview updated = reviewRepository.save(review);
        return toDTO(updated);
    }

    /**
     * Delete a performance review.
     */
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }

    // ---- Mapping helpers ----

    private PerformanceReviewDTO toDTO(PerformanceReview review) {
        return PerformanceReviewDTO.builder()
                .id(review.getId())
                .rating(review.getRating())
                .feedback(review.getFeedback())
                .reviewDate(review.getReviewDate())
                .employeeId(review.getEmployee().getId())
                .employeeName(review.getEmployee().getName())
                .build();
    }

    private PerformanceReview toEntity(PerformanceReviewDTO dto, Employee employee) {
        return PerformanceReview.builder()
                .rating(dto.getRating())
                .feedback(dto.getFeedback())
                .reviewDate(dto.getReviewDate())
                .employee(employee)
                .build();
    }
}
