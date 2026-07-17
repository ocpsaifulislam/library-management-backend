package dev.shoaibsuad.library_management.auth.entity;

import dev.shoaibsuad.library_management.common.entity.Auditable;
import dev.shoaibsuad.library_management.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken extends BaseEntity implements Auditable {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    /**
     * User that owns this refresh token.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * SHA-256 hash of the raw refresh token.
     */
    @Column(name = "token_hash", nullable = false, length = 64, unique = true)
    private String tokenHash;

    /**
     * Timestamp after which the refresh token cannot be used.
     */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /**
     * Indicates whether this token has been explicitly revoked.
     */
    @Column(nullable = false)
    private Boolean revoked = false;

    /**
     * Timestamp when the refresh token was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the refresh token was last modified.
     */
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    /**
     * Identifier of the user that created this refresh token.
     */
    @Column(name = "created_by")
    private Long createdBy;

    /**
     * Identifier of the user that last modified this refresh token.
     */
    @Column(name = "modified_by")
    private Long modifiedBy;
}
