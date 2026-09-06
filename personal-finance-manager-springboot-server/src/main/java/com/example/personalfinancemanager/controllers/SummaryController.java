package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.SummaryResponse;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final SummaryService summaryService;

    @Autowired
    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    /**
     * GET /api/summary
     * Optional query params: ?year=2025&month=8
     * - Both year + month → monthly summary
     * - Only year → full year summary
     * - Neither → all-time summary
     */
    @GetMapping
    public ResponseEntity<SummaryResponse> getSummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SummaryResponse summary = summaryService.getSummary(userDetails.getUser(), year, month);
        return ResponseEntity.ok(summary);
    }
}
