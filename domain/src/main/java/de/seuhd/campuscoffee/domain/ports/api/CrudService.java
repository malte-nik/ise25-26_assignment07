package de.seuhd.campuscoffee.domain.ports.api;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Generic service interface providing common CRUD operations.
 *
 * @param <DOMAIN> the domain object type managed by this service
 * @param <ID>     the type of the unique identifier (e.g., Long, UUID, String)
 */
public interface CrudService<DOMAIN extends DomainModel<ID>, ID> {
    /**
     * Clears all objects (i.e., deleted them).
     * Warning: This is a destructive operation typically used only for testing
     * or administrative purposes. Use with caution in production environments.
     */
    void clear();

    /**
     * Retrieves all objects.
     *
     * @return a list of all objects; never null, but may be empty if no objects exist
     */
    @NonNull List<DOMAIN> getAll();

    /**
     * Retrieves a specific object by its unique identifier.
     *
     * @param id the unique identifier of the object to retrieve; must not be null
     * @return the object with the specified ID; never null
     * @throws NotFoundException if no object exists with the given ID
     */
    @NonNull DOMAIN getById(@NonNull ID id);

    /**
     * Creates a new object or updates an existing one.
     * This method performs an "upsert" operation:
     * <ul>
     *   <li>If the object has no ID (null), a new object is created</li>
     *   <li>If the object has an ID, and it exists, the existing object is updated</li>
     * </ul>
     *
     * @param object the object to create or update; must not be null
     * @return the persisted object with populated ID and timestamps; never null
     * @throws NotFoundException if attempting to update an object that does not exist
     * @throws DuplicationException if an object with duplicate unique fields already exists
     */
    @NonNull DOMAIN upsert(@NonNull DOMAIN object);

    /**
     * Deletes an object by its unique identifier.
     *
     * @param id the unique identifier of the object to delete; must not be null
     * @throws NotFoundException if no object exists with the given ID
     */
    void delete(@NonNull ID id);
}
