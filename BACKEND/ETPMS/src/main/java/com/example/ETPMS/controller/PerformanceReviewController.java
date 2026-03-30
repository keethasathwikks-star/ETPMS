package com.example.ETPMS.controller;

import com.example.ETPMS.dto.PerformanceReviewDTO;
import com.example.ETPMS.service.PerformanceReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for Performance Review CRUD operations.
 * Base path: /api/reviews
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PerformanceReviewController {

    private final PerformanceReviewService reviewService;

    /** GET /api/reviews — List all reviews */
    @GetMapping
    public ResponseEntity<List<PerformanceReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /** GET /api/reviews/{id} — Get review by ID */
    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReviewDTO> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    /**
     * GET /api/reviews/employee/{employeeId} — Get reviews for a specific employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PerformanceReviewDTO>> getReviewsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(reviewService.getReviewsByEmployeeId(employeeId));
    }

    /** POST /api/reviews — Create a new review */
    @PostMapping
    public ResponseEntity<PerformanceReviewDTO> createReview(@Valid @RequestBody PerformanceReviewDTO dto) {
        PerformanceReviewDTO created = reviewService.createReview(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** PUT /api/reviews/{id} — Update a review */
    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReviewDTO> updateReview(@PathVariable Long id,
            @Valid @RequestBody PerformanceReviewDTO dto) {
        return ResponseEntity.ok(reviewService.updateReview(id, dto));
    }

    /** DELETE /api/reviews/{id} — Delete a review */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
