package com.skillgap.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillgap.dto.GapAnalysisResponse;
import com.skillgap.dto.GapAnalysisResponse.SkillGapDetail;
import com.skillgap.dto.LearningPathResponse;
import com.skillgap.dto.LearningPathResponse.LearningStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private static final Logger log = LoggerFactory.getLogger(GeminiService.class);

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    public GeminiService() {
        this.webClient = WebClient.builder()
                .codecs(config -> config.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }

    /**
     * Generate an AI-powered learning path using Google Gemini
     */
    public LearningPathResponse generateLearningPath(GapAnalysisResponse gapAnalysis) {
        String prompt = buildPrompt(gapAnalysis);
        log.debug("Sending prompt to Gemini AI for role: {}", gapAnalysis.getRoleName());

        try {
            String aiResponse = callGeminiApi(prompt);
            LearningPathResponse response = parseAiResponse(aiResponse, gapAnalysis.getRoleName());
            response.setRawAiResponse(aiResponse);
            return response;
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage(), e);
            return buildFallbackResponse(gapAnalysis);
        }
    }

    private String buildPrompt(GapAnalysisResponse gap) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a career mentor and learning path expert. ");
        sb.append("Generate a detailed, structured learning path for someone who wants to become a **")
                .append(gap.getRoleName()).append("**.\n\n");

        sb.append("## Current Skill Analysis:\n\n");

        if (!gap.getMatchedSkills().isEmpty()) {
            sb.append("### Skills already mastered:\n");
            for (SkillGapDetail s : gap.getMatchedSkills()) {
                sb.append("- ").append(s.getSkillName())
                        .append(" (Current: ").append(s.getUserLevel())
                        .append(", Required: ").append(s.getRequiredLevel()).append(")\n");
            }
            sb.append("\n");
        }

        if (!gap.getPartialSkills().isEmpty()) {
            sb.append("### Skills that need improvement:\n");
            for (SkillGapDetail s : gap.getPartialSkills()) {
                sb.append("- ").append(s.getSkillName())
                        .append(" (Current: ").append(s.getUserLevel())
                        .append(", Required: ").append(s.getRequiredLevel())
                        .append(", Gap: ").append(s.getGap()).append(")\n");
            }
            sb.append("\n");
        }

        if (!gap.getMissingSkills().isEmpty()) {
            sb.append("### Skills completely missing:\n");
            for (SkillGapDetail s : gap.getMissingSkills()) {
                sb.append("- ").append(s.getSkillName())
                        .append(" (Required level: ").append(s.getRequiredLevel()).append(")\n");
            }
            sb.append("\n");
        }

        sb.append("## Instructions:\n");
        sb.append("Generate a step-by-step learning path with exactly this JSON format. ");
        sb.append("Do NOT include markdown code fences or any text outside the JSON.\n\n");
        sb.append("{\n");
        sb.append("  \"overview\": \"A 2-3 sentence overview of the learning journey\",\n");
        sb.append("  \"totalEstimatedWeeks\": <number>,\n");
        sb.append("  \"learningSteps\": [\n");
        sb.append("    {\n");
        sb.append("      \"stepNumber\": 1,\n");
        sb.append("      \"title\": \"Step title\",\n");
        sb.append("      \"description\": \"Detailed description of what to learn and why\",\n");
        sb.append("      \"duration\": \"2 weeks\",\n");
        sb.append("      \"resources\": [\"Resource 1\", \"Resource 2\"],\n");
        sb.append("      \"priority\": \"HIGH\"\n");
        sb.append("    }\n");
        sb.append("  ]\n");
        sb.append("}\n\n");
        sb.append("Rules:\n");
        sb.append("- Focus on missing and partial skills first (HIGH priority)\n");
        sb.append("- Include 5-8 learning steps\n");
        sb.append("- Each step should have 2-3 real, specific resources (course names, documentation links, books)\n");
        sb.append("- Be realistic with time estimates\n");
        sb.append("- Order steps logically (prerequisites first)\n");
        sb.append("- Return ONLY valid JSON, no other text\n");

        return sb.toString();
    }

    private String callGeminiApi(String prompt) {
        String requestBody = String.format(
                "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
                prompt.replace("\"", "\\\"").replace("\n", "\\n"));

        String response = webClient.post()
                .uri(apiUrl + "?key=" + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.debug("Gemini API response received");
        return response;
    }

    private LearningPathResponse parseAiResponse(String rawResponse, String roleName) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String text = root.at("/candidates/0/content/parts/0/text").asText();

            // Clean up text — remove markdown code fences if present
            text = text.replace("```json", "").replace("```", "").trim();

            JsonNode pathJson = objectMapper.readTree(text);

            LearningPathResponse response = new LearningPathResponse();
            response.setRoleName(roleName);
            response.setOverview(pathJson.path("overview").asText(""));
            response.setTotalEstimatedWeeks(pathJson.path("totalEstimatedWeeks").asInt(12));

            List<LearningStep> steps = new ArrayList<>();
            JsonNode stepsNode = pathJson.path("learningSteps");
            if (stepsNode.isArray()) {
                for (JsonNode stepNode : stepsNode) {
                    LearningStep step = new LearningStep();
                    step.setStepNumber(stepNode.path("stepNumber").asInt());
                    step.setTitle(stepNode.path("title").asText("Untitled Step"));
                    step.setDescription(stepNode.path("description").asText(""));
                    step.setDuration(stepNode.path("duration").asText("1-2 weeks"));
                    step.setPriority(stepNode.path("priority").asText("MEDIUM"));

                    List<String> resources = new ArrayList<>();
                    JsonNode resourcesNode = stepNode.path("resources");
                    if (resourcesNode.isArray()) {
                        for (JsonNode r : resourcesNode) {
                            resources.add(r.asText());
                        }
                    }
                    step.setResources(resources);
                    steps.add(step);
                }
            }

            response.setLearningSteps(steps);
            return response;

        } catch (Exception e) {
            log.error("Failed to parse Gemini response: {}", e.getMessage());
            throw new RuntimeException("Failed to parse AI response", e);
        }
    }

    /**
     * Fallback response if Gemini API fails
     */
    private LearningPathResponse buildFallbackResponse(GapAnalysisResponse gap) {
        LearningPathResponse response = new LearningPathResponse();
        response.setRoleName(gap.getRoleName());
        response.setOverview(
                "AI-generated path is temporarily unavailable. Here's a basic learning roadmap based on your gap analysis.");
        response.setTotalEstimatedWeeks(12);

        List<LearningStep> steps = new ArrayList<>();
        int stepNum = 1;

        // Add missing skills as high priority steps
        for (SkillGapDetail skill : gap.getMissingSkills()) {
            LearningStep step = new LearningStep();
            step.setStepNumber(stepNum++);
            step.setTitle("Learn " + skill.getSkillName());
            step.setDescription(
                    "This skill is completely missing from your profile. Start from the basics and work up to level "
                            + skill.getRequiredLevel() + ".");
            step.setDuration("2-3 weeks");
            step.setPriority("HIGH");
            step.setResources(Arrays.asList(
                    "Search for '" + skill.getSkillName() + " tutorial for beginners' on YouTube",
                    "Official " + skill.getSkillName() + " documentation"));
            steps.add(step);
        }

        // Add partial skills as medium priority steps
        for (SkillGapDetail skill : gap.getPartialSkills()) {
            LearningStep step = new LearningStep();
            step.setStepNumber(stepNum++);
            step.setTitle("Improve " + skill.getSkillName());
            step.setDescription("Improve from level " + skill.getUserLevel() + " to level " + skill.getRequiredLevel()
                    + ". Focus on advanced concepts and real-world projects.");
            step.setDuration("1-2 weeks");
            step.setPriority("MEDIUM");
            step.setResources(Arrays.asList(
                    "Practice " + skill.getSkillName() + " through hands-on projects",
                    "Take an intermediate/advanced " + skill.getSkillName() + " course"));
            steps.add(step);
        }

        response.setLearningSteps(steps);
        response.setRawAiResponse("Fallback response — Gemini API unavailable");
        return response;
    }
}
