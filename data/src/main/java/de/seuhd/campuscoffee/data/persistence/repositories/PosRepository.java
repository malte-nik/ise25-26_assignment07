package de.seuhd.campuscoffee.data.persistence.repositories;

import de.seuhd.campuscoffee.data.persistence.entities.PosEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for persisting point-of-sale (POS) entities.
 */
public interface PosRepository extends JpaRepository<PosEntity, Long>, ResettableSequenceRepository {
    Optional<PosEntity> findByName(String name);
}
