package dev.shoaibsuad.library_management.auth.repository;

import dev.shoaibsuad.library_management.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for persisted refresh token operations.
 *
 * @author Saiful Islam
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Finds a refresh token record by its SHA-256 token hash.
     *
     * @param tokenHash hash of the raw refresh token
     * @return matching token when present
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);
}
