package com.skillgap.controller;

import com.skillgap.entity.JobRole;
import com.skillgap.entity.RoleSkillRequirement;
import com.skillgap.service.JobRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:5173")
public class JobRoleController {

    @Autowired
    private JobRoleService jobRoleService;

    @GetMapping
    public ResponseEntity<List<JobRole>> getAllRoles() {
        return ResponseEntity.ok(jobRoleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobRole> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(jobRoleService.getRoleById(id));
    }

    @GetMapping("/{id}/requirements")
    public ResponseEntity<List<RoleSkillRequirement>> getRoleRequirements(@PathVariable Long id) {
        return ResponseEntity.ok(jobRoleService.getRoleRequirements(id));
    }
}
