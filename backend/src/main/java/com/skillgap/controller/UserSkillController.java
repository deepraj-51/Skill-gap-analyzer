package com.skillgap.controller;

import com.skillgap.dto.UserSkillDTO;
import com.skillgap.service.UserSkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-skills")
@CrossOrigin(origins = "http://localhost:5173")
public class UserSkillController {

    @Autowired
    private UserSkillService userSkillService;

    @GetMapping
    public ResponseEntity<List<UserSkillDTO>> getUserSkills(Authentication authentication) {
        // Extract user ID from authentication (username is stored in JWT)
        // For simplicity, we'll pass userId as a parameter
        // In production, you'd extract this from the JWT token
        return ResponseEntity.ok(userSkillService.getUserSkills(getUserId(authentication)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserSkillDTO>> getUserSkillsById(@PathVariable Long userId) {
        return ResponseEntity.ok(userSkillService.getUserSkills(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addUserSkill(
            @PathVariable Long userId,
            @Valid @RequestBody UserSkillDTO skillDTO) {
        try {
            UserSkillDTO result = userSkillService.addUserSkill(userId, skillDTO);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/skills/{skillId}")
    public ResponseEntity<?> updateUserSkill(
            @PathVariable Long userId,
            @PathVariable Long skillId,
            @RequestBody java.util.Map<String, Integer> body) {
        try {
            Integer proficiencyLevel = body.get("proficiencyLevel");
            UserSkillDTO result = userSkillService.updateUserSkill(userId, skillId, proficiencyLevel);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/skills/{skillId}")
    public ResponseEntity<?> deleteUserSkill(
            @PathVariable Long userId,
            @PathVariable Long skillId) {
        try {
            userSkillService.deleteUserSkill(userId, skillId);
            return ResponseEntity.ok(Map.of("message", "Skill deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private Long getUserId(Authentication authentication) {
        // This is a simplified version - in production, you'd extract user ID from JWT
        // For now, return a default value
        return 1L;
    }
}
