package dev.shoaibsuad.library_management.auth.service.impl;

import dev.shoaibsuad.library_management.auth.dto.request.RegisterRequest;
import dev.shoaibsuad.library_management.auth.dto.response.UserResponse;
import dev.shoaibsuad.library_management.auth.entity.User;
import dev.shoaibsuad.library_management.auth.mapper.UserMapper;
import dev.shoaibsuad.library_management.auth.repository.UserRepository;
import dev.shoaibsuad.library_management.auth.service.AuthService;
import dev.shoaibsuad.library_management.common.exception.ResourceConflictException;
import dev.shoaibsuad.library_management.common.service.IdGeneratorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl  implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IdGeneratorService idGeneratorService;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ResourceConflictException("User with username '" + request.username() + "' already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceConflictException("User with email '" + request.email() + "' already exists.");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setId(idGeneratorService.getNewId("USERS"));
        user.setIsActive(1L);

        entityManager.persist(user);
        return userMapper.toResponse(user);
    }
}
