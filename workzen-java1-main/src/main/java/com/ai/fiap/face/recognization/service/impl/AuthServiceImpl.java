package com.ai.fiap.face.recognization.service.impl;

import com.ai.fiap.face.recognization.dto.request.LoginRequest;
import com.ai.fiap.face.recognization.dto.request.RegisterRequest;
import com.ai.fiap.face.recognization.dto.response.AuthResponse;
import com.ai.fiap.face.recognization.dto.response.UserResponse;
import com.ai.fiap.face.recognization.exception.BusinessException;
import com.ai.fiap.face.recognization.exception.UnauthorizedException;
import com.ai.fiap.face.recognization.mapper.UserMapper;
import com.ai.fiap.face.recognization.model.User;
import com.ai.fiap.face.recognization.repository.UserRepository;
import com.ai.fiap.face.recognization.service.AuthService;
import com.ai.fiap.face.recognization.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email já cadastrado");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = validateUser(request.getEmail(), request.getPassword());

        String token = jwtService.generateToken(user);
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Override
    public User validateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        return user;
    }
}

