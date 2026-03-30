package com.example.ETPMS.controller;

import com.example.ETPMS.dto.DashboardStatsDTO;
import com.example.ETPMS.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Dashboard statistics.
 * Base path: /api/dashboard
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DashboardController {

    private final DashboardService dashboardService;

    /** GET /api/dashboard/stats — Get aggregated statistics */
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
}
