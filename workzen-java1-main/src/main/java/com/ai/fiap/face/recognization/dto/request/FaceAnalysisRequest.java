package com.ai.fiap.face.recognization.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaceAnalysisRequest {

    @NotBlank(message = "Imagem em base64 é obrigatória")
    @JsonProperty("image_base64")
    private String imageBase64;
}

