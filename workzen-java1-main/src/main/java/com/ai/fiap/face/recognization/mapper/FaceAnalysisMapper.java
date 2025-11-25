package com.ai.fiap.face.recognization.mapper;

import com.ai.fiap.face.recognization.dto.request.FaceAnalysisRequest;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisUpdateRequest;
import com.ai.fiap.face.recognization.dto.response.FaceAnalysisResponse;
import com.ai.fiap.face.recognization.model.FaceAnalysis;
import com.ai.fiap.face.recognization.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FaceAnalysisMapper {

    @Mapping(source = "user.id", target = "userId")
    FaceAnalysisResponse toResponse(FaceAnalysis faceAnalysis);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "emotion", ignore = true)
    @Mapping(target = "analysisDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FaceAnalysis toEntity(FaceAnalysisRequest request, User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "analysisDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromRequest(FaceAnalysisUpdateRequest request, @MappingTarget FaceAnalysis faceAnalysis);
}

