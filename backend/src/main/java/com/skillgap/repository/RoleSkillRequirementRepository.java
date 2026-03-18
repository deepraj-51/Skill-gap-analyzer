package com.skillgap.repository;

import com.skillgap.entity.RoleSkillRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleSkillRequirementRepository extends JpaRepository<RoleSkillRequirement, Long> {
    List<RoleSkillRequirement> findByRoleId(Long roleId);
}
