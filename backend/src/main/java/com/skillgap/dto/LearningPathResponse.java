package com.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearningPathResponse {
    private String roleName;
    private String overview;
    private List<LearningStep> learningSteps;
    private int totalEstimatedWeeks;
    private String rawAiResponse;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LearningStep {
        private int stepNumber;
        private String title;
        private String description;
        private String duration;
        private List<String> resources;
        private String priority; // HIGH, MEDIUM, LOW
    }
}
