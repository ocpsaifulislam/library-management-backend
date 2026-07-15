package dev.shoaibsuad.library_management.auth.mapper;

import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;
import dev.shoaibsuad.library_management.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
}