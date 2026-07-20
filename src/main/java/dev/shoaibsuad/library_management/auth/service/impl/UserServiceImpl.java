package dev.shoaibsuad.library_management.auth.service.impl;

import dev.shoaibsuad.library_management.auth.entity.User;
import dev.shoaibsuad.library_management.auth.repository.UserRepository;
import dev.shoaibsuad.library_management.auth.service.UserService;
import dev.shoaibsuad.library_management.auth.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (user.getIsActive() == null || user.getIsActive() != 1L) {
            throw new DisabledException("User account is inactive.");
        }

        return new AuthenticatedUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                mapAuthorities());
    }

    private Set<SimpleGrantedAuthority> mapAuthorities() {
        return Collections.emptySet();
    }
}