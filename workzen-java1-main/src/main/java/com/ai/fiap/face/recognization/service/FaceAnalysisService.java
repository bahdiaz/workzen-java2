package com.ai.fiap.face.recognization.service;

import com.ai.fiap.face.recognization.dto.request.FaceAnalysisRequest;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisUpdateRequest;
import com.ai.fiap.face.recognization.dto.response.FaceAnalysisResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaceAnalysisService {

    FaceAnalysisResponse create(String token, FaceAnalysisRequest request);

    FaceAnalysisResponse getById(String token, Long id);

    FaceAnalysisResponse update(String token, Long id, FaceAnalysisUpdateRequest request);

    void delete(String token, Long id);

    Page<FaceAnalysisResponse> getAllByUser(String token, Pageable pageable);

    String analyzeFace(String imageBase64);
}

