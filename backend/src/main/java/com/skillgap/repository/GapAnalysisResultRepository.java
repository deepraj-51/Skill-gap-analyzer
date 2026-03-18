package com.skillgap.repository;

import com.skillgap.entity.GapAnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GapAnalysisResultRepository extends JpaRepository<GapAnalysisResult, Long> {
    List<GapAnalysisResult> findByUserIdOrderByAnalysisDateDesc(Long userId);
}
