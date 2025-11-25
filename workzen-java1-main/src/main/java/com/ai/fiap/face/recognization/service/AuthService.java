package com.ai.fiap.face.recognization.service;

import com.ai.fiap.face.recognization.dto.request.LoginRequest;
import com.ai.fiap.face.recognization.dto.request.RegisterRequest;
import com.ai.fiap.face.recognization.dto.response.AuthResponse;
import com.ai.fiap.face.recognization.model.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

   User validateUser(String email, String password);
}

