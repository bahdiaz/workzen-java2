package com.ai.fiap.face.recognization.service.impl;

import com.ai.fiap.face.recognization.client.DeepFaceClient;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisRequest;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisUpdateRequest;
import com.ai.fiap.face.recognization.dto.response.FaceAnalysisResponse;
import com.ai.fiap.face.recognization.exception.ResourceNotFoundException;
import com.ai.fiap.face.recognization.exception.UnauthorizedException;
import com.ai.fiap.face.recognization.mapper.FaceAnalysisMapper;
import com.ai.fiap.face.recognization.model.FaceAnalysis;
import com.ai.fiap.face.recognization.model.User;
import com.ai.fiap.face.recognization.repository.FaceAnalysisRepository;
import com.ai.fiap.face.recognization.repository.UserRepository;
import com.ai.fiap.face.recognization.service.FaceAnalysisService;
import com.ai.fiap.face.recognization.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaceAnalysisServiceImpl implements FaceAnalysisService {

    private final FaceAnalysisRepository faceAnalysisRepository;
    private final UserRepository userRepository;
    private final FaceAnalysisMapper faceAnalysisMapper;
    private final JwtService jwtService;
    private final DeepFaceClient deepFaceClient;

    @Override
    @Transactional
    @CacheEvict(value = "faceAnalyses", allEntries = true)
    public FaceAnalysisResponse create(String token, FaceAnalysisRequest request) {
        Long userId = jwtService.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));

        String emotion = analyzeFace(request.getImageBase64());

        FaceAnalysis faceAnalysis = faceAnalysisMapper.toEntity(request, user);
        faceAnalysis.setEmotion(emotion);
        faceAnalysis = faceAnalysisRepository.save(faceAnalysis);

        return faceAnalysisMapper.toResponse(faceAnalysis);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "faceAnalyses", key = "#id")
    public FaceAnalysisResponse getById(String token, Long id) {
        Long userId = jwtService.getUserIdFromToken(token);
        FaceAnalysis faceAnalysis = faceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análise facial não encontrada"));

        if (!faceAnalysis.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Acesso negado a esta análise");
        }

        return faceAnalysisMapper.toResponse(faceAnalysis);
    }

    @Override
    @Transactional
    @CacheEvict(value = "faceAnalyses", allEntries = true)
    public FaceAnalysisResponse update(String token, Long id, FaceAnalysisUpdateRequest request) {
        Long userId = jwtService.getUserIdFromToken(token);
        FaceAnalysis faceAnalysis = faceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análise facial não encontrada"));

        if (!faceAnalysis.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Acesso negado a esta análise");
        }

        faceAnalysisMapper.updateFromRequest(request, faceAnalysis);

        if (request.getImageBase64() != null && !request.getImageBase64().isEmpty()) {
            String emotion = analyzeFace(request.getImageBase64());
            faceAnalysis.setEmotion(emotion);
        }

        faceAnalysis = faceAnalysisRepository.save(faceAnalysis);
        return faceAnalysisMapper.toResponse(faceAnalysis);
    }

    @Override
    @Transactional
    @CacheEvict(value = "faceAnalyses", allEntries = true)
    public void delete(String token, Long id) {
        Long userId = jwtService.getUserIdFromToken(token);
        FaceAnalysis faceAnalysis = faceAnalysisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análise facial não encontrada"));

        if (!faceAnalysis.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Acesso negado a esta análise");
        }

        faceAnalysisRepository.delete(faceAnalysis);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "faceAnalyses", key = "#token + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<FaceAnalysisResponse> getAllByUser(String token, Pageable pageable) {
        Long userId = jwtService.getUserIdFromToken(token);
        Page<FaceAnalysis> faceAnalyses = faceAnalysisRepository
                .findByUserId(userId, pageable);

        return faceAnalyses.map(faceAnalysisMapper::toResponse);
    }

    @Override
    public String analyzeFace(String imageBase64) {
        return deepFaceClient.analyzeEmotion(imageBase64);
    }
}

