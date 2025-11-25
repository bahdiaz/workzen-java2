package com.ai.fiap.face.recognization.repository;

import com.ai.fiap.face.recognization.model.FaceAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaceAnalysisRepository extends JpaRepository<FaceAnalysis, Long> {

    Page<FaceAnalysis> findByUserId(Long userId, Pageable pageable);

}
