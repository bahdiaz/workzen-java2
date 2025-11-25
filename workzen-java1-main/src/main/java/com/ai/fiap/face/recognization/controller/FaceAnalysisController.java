package com.ai.fiap.face.recognization.controller;

import com.ai.fiap.face.recognization.dto.request.FaceAnalysisRequest;
import com.ai.fiap.face.recognization.dto.request.FaceAnalysisUpdateRequest;
import com.ai.fiap.face.recognization.dto.response.FaceAnalysisResponse;
import com.ai.fiap.face.recognization.service.FaceAnalysisService;
import com.ai.fiap.face.recognization.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/face")
@RequiredArgsConstructor
@Tag(name = "Análise Facial", description = "APIs para gerenciamento de análises faciais")
public class FaceAnalysisController {

    private final FaceAnalysisService faceAnalysisService;
    private final JwtService jwtService;

    @PostMapping("/analyse")
    @Operation(summary = "Criar análise facial", description = "Envia imagem em base64 e retorna análise de emoção")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Análise criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<FaceAnalysisResponse> create(
            @Parameter(description = "Token JWT no formato Bearer", required = true)
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody FaceAnalysisRequest request) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        FaceAnalysisResponse response = faceAnalysisService.create(token, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar análise por ID", description = "Retorna uma análise facial específica")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise encontrada"),
            @ApiResponse(responseCode = "404", description = "Análise não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<FaceAnalysisResponse> getById(
            @Parameter(description = "Token JWT no formato Bearer", required = true)
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        FaceAnalysisResponse response = faceAnalysisService.getById(token, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar análise", description = "Atualiza uma análise facial existente")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Análise atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Análise não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<FaceAnalysisResponse> update(
            @Parameter(description = "Token JWT no formato Bearer", required = true)
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody FaceAnalysisUpdateRequest request) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        FaceAnalysisResponse response = faceAnalysisService.update(token, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar análise", description = "Remove uma análise facial")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Análise deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Análise não encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Token JWT no formato Bearer", required = true)
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        faceAnalysisService.delete(token, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar análises", description = "Retorna lista paginada de análises do usuário")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<Page<FaceAnalysisResponse>> getAll(
            @Parameter(description = "Token JWT no formato Bearer", required = true)
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Parâmetros de paginação") Pageable pageable) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        Page<FaceAnalysisResponse> response = faceAnalysisService.getAllByUser(token, pageable);
        return ResponseEntity.ok(response);
    }
}

