package com.ai.fiap.face.recognization.service;

import com.ai.fiap.face.recognization.model.User;

public interface JwtService {

    String generateToken(User user);

    Boolean validateToken(String token);

    Long getUserIdFromToken(String token);

    String extractTokenFromHeader(String authHeader);
}

