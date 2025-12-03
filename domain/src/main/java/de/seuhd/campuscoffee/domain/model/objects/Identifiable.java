package de.seuhd.campuscoffee.domain.model.objects;

import org.jspecify.annotations.Nullable;

/**
 * Interface for domain objects and DTOs that have an identifier.
 * This interface enables generic CRUD operations in base controllers
 * by providing a standard way to access the ID.
 */
public interface Identifiable<T> {
    /**
     * Returns the unique identifier of this DTO.
     *
     * @return the ID, or null if the resource has not been created yet
     */
    @Nullable T getId();
}
