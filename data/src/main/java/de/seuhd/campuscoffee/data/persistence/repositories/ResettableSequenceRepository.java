package de.seuhd.campuscoffee.data.persistence.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;

/**
 * Interface for repositories that support sequence resetting.
 * This is primarily used in test scenarios to ensure consistent IDs.
 */
public interface ResettableSequenceRepository {
    /**
     * Resets the database sequence for this entity's ID generation.
     * This method is used in tests to ensure consistent and predictable IDs.
     * Note: The implementation must be provided in the concrete repository interface.
     */
    @Modifying
    @Transactional
    void resetSequence();
}
