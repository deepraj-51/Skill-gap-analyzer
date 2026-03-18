package com.skillgap.service;

import com.skillgap.dto.GapAnalysisResponse.SkillGapDetail;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    /**
     * Generates structured learning recommendations based on gap analysis
     */
    public List<String> generateRecommendations(
            List<SkillGapDetail> matched,
            List<SkillGapDetail> partial,
            List<SkillGapDetail> missing) {
        List<String> recommendations = new ArrayList<>();

        // Priority 1: Missing critical skills
        if (!missing.isEmpty()) {
            recommendations.add("🎯 PRIORITY: Learn the following missing skills:");

            // Group by category
            Map<String, List<SkillGapDetail>> missingByCategory = missing.stream()
                    .collect(Collectors.groupingBy(SkillGapDetail::getCategory));

            for (Map.Entry<String, List<SkillGapDetail>> entry : missingByCategory.entrySet()) {
                String category = entry.getKey();
                List<String> skillNames = entry.getValue().stream()
                        .map(s -> s.getSkillName() + " (Level " + s.getRequiredLevel() + ")")
                        .collect(Collectors.toList());
                recommendations.add("  • " + category + ": " + String.join(", ", skillNames));
            }
        }

        // Priority 2: Improve partial skills
        if (!partial.isEmpty()) {
            recommendations.add("\n📈 IMPROVE: Strengthen these existing skills:");

            // Sort by gap (largest first)
            partial.sort((a, b) -> b.getGap().compareTo(a.getGap()));

            for (SkillGapDetail skill : partial) {
                recommendations.add(String.format(
                        "  • %s: Improve from Level %d to Level %d (+%d levels)",
                        skill.getSkillName(),
                        skill.getUserLevel(),
                        skill.getRequiredLevel(),
                        skill.getGap()));
            }
        }

        // Priority 3: Maintain matched skills
        if (!matched.isEmpty()) {
            recommendations.add("\n✅ MAINTAIN: You already meet requirements for:");
            List<String> matchedNames = matched.stream()
                    .map(SkillGapDetail::getSkillName)
                    .collect(Collectors.toList());
            recommendations.add("  • " + String.join(", ", matchedNames));
        }

        // Overall summary
        int totalRequired = matched.size() + partial.size() + missing.size();
        int readySkills = matched.size();
        double readinessPercentage = totalRequired > 0 ? (readySkills * 100.0 / totalRequired) : 0;

        recommendations.add(String.format(
                "\n📊 READINESS: You are %.1f%% ready for this role (%d/%d skills met)",
                readinessPercentage,
                readySkills,
                totalRequired));

        // Actionable next steps
        if (!missing.isEmpty() || !partial.isEmpty()) {
            recommendations.add("\n💡 NEXT STEPS:");
            if (!missing.isEmpty()) {
                SkillGapDetail topMissing = missing.get(0);
                recommendations.add(
                        "  1. Start learning " + topMissing.getSkillName() + " (" + topMissing.getCategory() + ")");
            }
            if (!partial.isEmpty()) {
                SkillGapDetail topPartial = partial.stream()
                        .max((a, b) -> a.getGap().compareTo(b.getGap()))
                        .orElse(partial.get(0));
                recommendations.add("  2. Practice " + topPartial.getSkillName() + " to reach Level "
                        + topPartial.getRequiredLevel());
            }
            recommendations.add("  3. Build projects that combine multiple required skills");
            recommendations.add("  4. Seek mentorship or take online courses in weak areas");
        } else {
            recommendations.add("\n🎉 Congratulations! You meet all requirements for this role!");
        }

        return recommendations;
    }
}
