package com.ai.fiap.face.recognization.mapper;

import com.ai.fiap.face.recognization.dto.request.RegisterRequest;
import com.ai.fiap.face.recognization.dto.response.UserResponse;
import com.ai.fiap.face.recognization.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);
}

