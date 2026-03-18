package com.skillgap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GapAnalysisResponse {
    private Long analysisId;
    private String roleName;
    private String roleDescription;
    private List<SkillGapDetail> matchedSkills;
    private List<SkillGapDetail> partialSkills;
    private List<SkillGapDetail> missingSkills;
    private List<String> recommendations;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SkillGapDetail {
        private String skillName;
        private String category;
        private Integer userLevel;
        private Integer requiredLevel;
        private Integer gap;
    }
}
