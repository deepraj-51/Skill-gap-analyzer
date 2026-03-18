package com.skillgap.service;

import com.skillgap.dto.UserSkillDTO;
import com.skillgap.entity.Skill;
import com.skillgap.entity.User;
import com.skillgap.entity.UserSkill;
import com.skillgap.repository.SkillRepository;
import com.skillgap.repository.UserRepository;
import com.skillgap.repository.UserSkillRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSkillService {

    @Autowired
    private UserSkillRepository userSkillRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    public List<UserSkillDTO> getUserSkills(Long userId) {
        List<UserSkill> userSkills = userSkillRepository.findByUserId(userId);
        return userSkills.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserSkillDTO addUserSkill(Long userId, UserSkillDTO skillDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Skill skill = skillRepository.findById(skillDTO.getSkillId())
                .orElseThrow(() -> new RuntimeException("Skill not found"));

        // Check if user already has this skill
        if (userSkillRepository.findByUserIdAndSkillId(userId, skillDTO.getSkillId()).isPresent()) {
            throw new RuntimeException("User already has this skill. Use update instead.");
        }

        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setProficiencyLevel(skillDTO.getProficiencyLevel());

        userSkill = userSkillRepository.save(userSkill);
        return convertToDTO(userSkill);
    }

    public UserSkillDTO updateUserSkill(Long userId, Long skillId, Integer proficiencyLevel) {
        UserSkill userSkill = userSkillRepository.findByUserIdAndSkillId(userId, skillId)
                .orElseThrow(() -> new RuntimeException("User skill not found"));

        userSkill.setProficiencyLevel(proficiencyLevel);
        userSkill = userSkillRepository.save(userSkill);
        return convertToDTO(userSkill);
    }

    @Transactional
    public void deleteUserSkill(Long userId, Long skillId) {
        userSkillRepository.deleteByUserIdAndSkillId(userId, skillId);
    }

    private UserSkillDTO convertToDTO(UserSkill userSkill) {
        UserSkillDTO dto = new UserSkillDTO();
        dto.setId(userSkill.getId());
        dto.setSkillId(userSkill.getSkill().getId());
        dto.setSkillName(userSkill.getSkill().getName());
        dto.setSkillCategory(userSkill.getSkill().getCategory());
        dto.setProficiencyLevel(userSkill.getProficiencyLevel());
        return dto;
    }
}
