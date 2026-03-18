package com.skillgap.controller;

import com.skillgap.dto.GapAnalysisResponse;
import com.skillgap.dto.LearningPathResponse;
import com.skillgap.service.GapAnalysisService;
import com.skillgap.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/learning-path")
@CrossOrigin(origins = "http://localhost:5173")
public class LearningPathController {

    @Autowired
    private GapAnalysisService gapAnalysisService;

    @Autowired
    private GeminiService geminiService;

    /**
     * Generate an AI-powered learning path for a user and target role
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateLearningPath(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long roleId = request.get("roleId");

            if (userId == null || roleId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "userId and roleId are required"));
            }

            // First, run gap analysis
            GapAnalysisResponse gapAnalysis = gapAnalysisService.analyzeGap(userId, roleId);

            // Then, send to Gemini AI for learning path generation
            LearningPathResponse learningPath = geminiService.generateLearningPath(gapAnalysis);

            return ResponseEntity.ok(learningPath);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to generate learning path: " + e.getMessage()));
        }
    }
}
