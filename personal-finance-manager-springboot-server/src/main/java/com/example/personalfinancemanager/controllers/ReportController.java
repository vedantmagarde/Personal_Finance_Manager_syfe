package com.example.personalfinancemanager.controllers;

import com.example.personalfinancemanager.dtos.MonthlyReportResponse;
import com.example.personalfinancemanager.dtos.YearlyReportResponse;
import com.example.personalfinancemanager.security.CustomUserDetails;
import com.example.personalfinancemanager.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<MonthlyReportResponse> getMonthlyReport(
            @PathVariable Integer year,
            @PathVariable Integer month,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month + ". Month must be between 1 and 12.");
        }
        if (year < 1) {
            throw new IllegalArgumentException("Invalid year: " + year);
        }
        MonthlyReportResponse report = reportService.getMonthlyReport(userDetails.getUser(), year, month);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/yearly/{year}")
    public ResponseEntity<YearlyReportResponse> getYearlyReport(
            @PathVariable Integer year,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        YearlyReportResponse report = reportService.getYearlyReport(userDetails.getUser(), year);
        return ResponseEntity.ok(report);
    }
}
