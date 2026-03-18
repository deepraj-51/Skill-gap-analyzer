package com.skillgap.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSkillDTO {
    private Long id;

    @NotNull(message = "Skill ID is required")
    private Long skillId;

    private String skillName;
    private String skillCategory;

    @NotNull(message = "Proficiency level is required")
    @Min(value = 1, message = "Proficiency level must be between 1 and 5")
    @Max(value = 5, message = "Proficiency level must be between 1 and 5")
    private Integer proficiencyLevel;
}
