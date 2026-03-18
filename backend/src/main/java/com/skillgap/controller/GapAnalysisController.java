package com.skillgap.controller;

import com.skillgap.dto.GapAnalysisResponse;
import com.skillgap.service.GapAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "http://localhost:5173")
public class GapAnalysisController {

    @Autowired
    private GapAnalysisService gapAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeGap(
            @RequestParam Long userId,
            @RequestParam Long roleId) {
        try {
            GapAnalysisResponse response = gapAnalysisService.analyzeGap(userId, roleId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getAnalysisHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(gapAnalysisService.getUserAnalysisHistory(userId));
    }
}
