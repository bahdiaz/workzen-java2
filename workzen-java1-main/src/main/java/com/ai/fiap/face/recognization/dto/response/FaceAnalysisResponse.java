package com.ai.fiap.face.recognization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceAnalysisResponse {

    private Long id;
    private Long userId;
    private String emotion;
    private LocalDateTime analysisDate;
    private LocalDateTime createdAt;
}

