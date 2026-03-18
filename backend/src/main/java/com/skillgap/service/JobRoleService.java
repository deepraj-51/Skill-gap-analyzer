package com.skillgap.service;

import com.skillgap.entity.JobRole;
import com.skillgap.entity.RoleSkillRequirement;
import com.skillgap.repository.JobRoleRepository;
import com.skillgap.repository.RoleSkillRequirementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRoleService {

    @Autowired
    private JobRoleRepository jobRoleRepository;

    @Autowired
    private RoleSkillRequirementRepository roleSkillRequirementRepository;

    public List<JobRole> getAllRoles() {
        return jobRoleRepository.findAll();
    }

    public JobRole getRoleById(Long roleId) {
        return jobRoleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public List<RoleSkillRequirement> getRoleRequirements(Long roleId) {
        return roleSkillRequirementRepository.findByRoleId(roleId);
    }
}
