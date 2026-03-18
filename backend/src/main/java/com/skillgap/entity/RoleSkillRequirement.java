package com.skillgap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_skill_requirements", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"role_id", "skill_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleSkillRequirement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private JobRole role;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;
    
    @Column(name = "required_proficiency_level", nullable = false)
    private Integer requiredProficiencyLevel; // 1-5 scale
}
