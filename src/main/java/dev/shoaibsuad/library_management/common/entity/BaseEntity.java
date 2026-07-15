package dev.shoaibsuad.library_management.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @PrePersist
    protected void onCreate() {
        if (this instanceof Auditable auditable) {
            LocalDateTime newDate = LocalDateTime.now();
            auditable.setCreatedAt(newDate);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (this instanceof Auditable auditable) {
            LocalDateTime newDate = LocalDateTime.now();
            auditable.setModifiedAt(newDate);
        }
    }
}
