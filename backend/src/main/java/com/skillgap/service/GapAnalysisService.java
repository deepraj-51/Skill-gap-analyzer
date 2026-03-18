package com.skillgap.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillgap.dto.GapAnalysisResponse;
import com.skillgap.dto.GapAnalysisResponse.SkillGapDetail;
import com.skillgap.entity.*;
import com.skillgap.repository.GapAnalysisResultRepository;
import com.skillgap.repository.RoleSkillRequirementRepository;
import com.skillgap.repository.UserRepository;
import com.skillgap.repository.UserSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GapAnalysisService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private RoleSkillRequirementRepository roleSkillRequirementRepository;

    @Autowired
    private GapAnalysisResultRepository gapAnalysisResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRoleService jobRoleService;

    @Autowired
    private RecommendationService recommendationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Core gap analysis engine - compares user skills with role requirements
     */
    public GapAnalysisResponse analyzeGap(Long userId, Long roleId) {
        // Get user skills
        List<UserSkill> userSkills = userSkillRepository.findByUserId(userId);
        Map<Long, Integer> userSkillMap = userSkills.stream()
                .collect(Collectors.toMap(
                        us -> us.getSkill().getId(),
                        UserSkill::getProficiencyLevel));

        // Get role requirements
        List<RoleSkillRequirement> roleRequirements = roleSkillRequirementRepository.findByRoleId(roleId);
        JobRole role = jobRoleService.getRoleById(roleId);

        // Categorize skills
        List<SkillGapDetail> matchedSkills = new ArrayList<>();
        List<SkillGapDetail> partialSkills = new ArrayList<>();
        List<SkillGapDetail> missingSkills = new ArrayList<>();

        for (RoleSkillRequirement requirement : roleRequirements) {
            Skill skill = requirement.getSkill();
            Integer requiredLevel = requirement.getRequiredProficiencyLevel();
            Integer userLevel = userSkillMap.get(skill.getId());

            SkillGapDetail detail = new SkillGapDetail();
            detail.setSkillName(skill.getName());
            detail.setCategory(skill.getCategory());
            detail.setRequiredLevel(requiredLevel);

            if (userLevel == null) {
                // User doesn't have this skill
                detail.setUserLevel(0);
                detail.setGap(requiredLevel);
                missingSkills.add(detail);
            } else if (userLevel >= requiredLevel) {
                // User meets or exceeds requirement
                detail.setUserLevel(userLevel);
                detail.setGap(0);
                matchedSkills.add(detail);
            } else {
                // User has skill but below required level
                detail.setUserLevel(userLevel);
                detail.setGap(requiredLevel - userLevel);
                partialSkills.add(detail);
            }
        }

        // Generate recommendations
        List<String> recommendations = recommendationService.generateRecommendations(
                matchedSkills, partialSkills, missingSkills);

        // Save analysis result
        GapAnalysisResult result = saveAnalysisResult(
                userId, roleId, matchedSkills, partialSkills, missingSkills);

        // Build response
        GapAnalysisResponse response = new GapAnalysisResponse();
        response.setAnalysisId(result.getId());
        response.setRoleName(role.getName());
        response.setRoleDescription(role.getDescription());
        response.setMatchedSkills(matchedSkills);
        response.setPartialSkills(partialSkills);
        response.setMissingSkills(missingSkills);
        response.setRecommendations(recommendations);

        return response;
    }

    private GapAnalysisResult saveAnalysisResult(
            Long userId, Long roleId,
            List<SkillGapDetail> matched,
            List<SkillGapDetail> partial,
            List<SkillGapDetail> missing) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        JobRole role = jobRoleService.getRoleById(roleId);

        GapAnalysisResult result = new GapAnalysisResult();
        result.setUser(user);
        result.setRole(role);

        try {
            result.setMatchedSkills(objectMapper.writeValueAsString(matched));
            result.setPartialSkills(objectMapper.writeValueAsString(partial));
            result.setMissingSkills(objectMapper.writeValueAsString(missing));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing analysis results", e);
        }

        return gapAnalysisResultRepository.save(result);
    }

    public List<GapAnalysisResult> getUserAnalysisHistory(Long userId) {
        return gapAnalysisResultRepository.findByUserIdOrderByAnalysisDateDesc(userId);
    }
}
