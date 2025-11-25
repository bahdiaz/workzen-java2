package com.ai.fiap.face.recognization.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceAnalysisUpdateRequest {

    private String imageBase64;
    private String emotion;
}

