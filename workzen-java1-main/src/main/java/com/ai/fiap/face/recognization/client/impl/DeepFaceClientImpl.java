package com.ai.fiap.face.recognization.client.impl;

import com.ai.fiap.face.recognization.client.DeepFaceClient;
import com.ai.fiap.face.recognization.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class DeepFaceClientImpl implements DeepFaceClient {

    private final WebClient webClient;

    @Value("${deepface.api.url}")
    private String deepFaceApiUrl;

    @Override
    public String analyzeEmotion(String imageBase64) {
        try {
            Map<String, String> requestBody = Map.of("image_base64", imageBase64);

            String emotion = webClient.post()
                    .uri(deepFaceApiUrl + "/face/analyze")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(response -> (String) response.get("emotion"))
                    .onErrorResume(e -> Mono.error(new BusinessException("Erro ao analisar imagem: " + e.getMessage())))
                    .block();

            return emotion != null ? emotion : "unknown";
        } catch (Exception e) {
            throw new BusinessException("Erro ao comunicar com API de análise facial: " + e.getMessage());
        }
    }
}

