package com.ai.fiap.face.recognization.mapper;

import com.ai.fiap.face.recognization.dto.request.FaceAnalysisRequest;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisUpdateRequest;
import com.ai.fiap.face.recognization.dto.response.FaceAnalysisResponse;
import com.ai.fiap.face.recognization.model.FaceAnalysis;
import com.ai.fiap.face.recognization.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-22T23:29:27-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class FaceAnalysisMapperImpl implements FaceAnalysisMapper {

    @Override
    public FaceAnalysisResponse toResponse(FaceAnalysis faceAnalysis) {
        if ( faceAnalysis == null ) {
            return null;
        }

        FaceAnalysisResponse.FaceAnalysisResponseBuilder faceAnalysisResponse = FaceAnalysisResponse.builder();

        faceAnalysisResponse.userId( faceAnalysisUserId( faceAnalysis ) );
        faceAnalysisResponse.id( faceAnalysis.getId() );
        faceAnalysisResponse.emotion( faceAnalysis.getEmotion() );
        faceAnalysisResponse.analysisDate( faceAnalysis.getAnalysisDate() );
        faceAnalysisResponse.createdAt( faceAnalysis.getCreatedAt() );

        return faceAnalysisResponse.build();
    }

    @Override
    public FaceAnalysis toEntity(FaceAnalysisRequest request, User user) {
        if ( request == null && user == null ) {
            return null;
        }

        FaceAnalysis.FaceAnalysisBuilder faceAnalysis = FaceAnalysis.builder();

        if ( request != null ) {
            faceAnalysis.imageBase64( request.getImageBase64() );
        }
        faceAnalysis.user( user );

        return faceAnalysis.build();
    }

    @Override
    public void updateFromRequest(FaceAnalysisUpdateRequest request, FaceAnalysis faceAnalysis) {
        if ( request == null ) {
            return;
        }

        faceAnalysis.setImageBase64( request.getImageBase64() );
        faceAnalysis.setEmotion( request.getEmotion() );
    }

    private Long faceAnalysisUserId(FaceAnalysis faceAnalysis) {
        if ( faceAnalysis == null ) {
            return null;
        }
        User user = faceAnalysis.getUser();
        if ( user == null ) {
            return null;
        }
        Long id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
