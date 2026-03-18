package com.skillgap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "gap_analysis_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GapAnalysisResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private JobRole role;
    
    @CreationTimestamp
    @Column(name = "analysis_date", updatable = false)
    private LocalDateTime analysisDate;
    
    @Column(columnDefinition = "TEXT")
    private String matchedSkills; // JSON array of matched skills
    
    @Column(columnDefinition = "TEXT")
    private String partialSkills; // JSON array of partially matched skills
    
    @Column(columnDefinition = "TEXT")
    private String missingSkills; // JSON array of missing skills
}
