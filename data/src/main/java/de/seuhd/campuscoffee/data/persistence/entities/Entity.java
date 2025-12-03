package de.seuhd.campuscoffee.data.persistence.entities;

import de.seuhd.campuscoffee.data.persistence.generators.CustomSequence;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Base entity class to provide common fields for all database entities.
 * This class includes createdAt and updatedAt timestamps, which are automatically
 * managed via JPA lifecycle callbacks.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Entity {
    @Id
    @GeneratedValue
    @CustomSequence
    private Long id;

    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback: set timestamps before persisting a new entity.
     * This ensures timestamps reflect actual database operation time.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        createdAt = now;
        updatedAt = now;
    }

    /**
     * JPA lifecycle callback: update timestamp before updating entity.
     * This ensures timestamps reflect actual database operation time.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
}
