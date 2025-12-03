package de.seuhd.campuscoffee.domain.ports.data;

import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Generic port interface for CRUD (Create, Read, Update, Delete) data operations.
 * <p>
 * This port is implemented by the data layer (adapter) and defines the contract
 * for basic persistence operations on entities.
 * Follows the hexagonal architecture pattern where the domain defines the port
 * and the data layer provides the adapter implementation.
 *
 * @param <DOMAIN> the domain model type
 * @param <ID>     the type of the unique identifier (e.g., Long, UUID, String)
 */
public interface CrudDataService<DOMAIN extends DomainModel<ID>, ID> {
    /**
     * Clears all data of this type from the data store.
     * This is typically used for testing or administrative purposes.
     * Warning: This operation is destructive and cannot be undone.
     */
    void clear();

    /**
     * Retrieves all entities from the data store and returns them as domain objects.
     *
     * @return a list of all entities; never null, but may be empty
     */
    @NonNull List<DOMAIN> getAll();

    /**
     * Retrieves a single entity by its unique identifier and returns it as a domain object.
     *
     * @param id the unique identifier of the entity to retrieve; must not be null
     * @return the entity with the specified ID; never null
     * @throws NotFoundException if no entity exists with the given ID
     */
    @NonNull DOMAIN getById(@NonNull ID id);

    /**
     * Creates a new entity or updates an existing one.
     * If the entity has an ID and exists in the data store, it will be updated.
     * If the entity has no ID (null), a new entity will be created.
     *
     * @param entity the entity to create or update; must not be null
     * @return the persisted entity with updated timestamps and ID as a domain object; never null
     * @throws NotFoundException if attempting to update an entity that does not exist
     */
    @NonNull DOMAIN upsert(@NonNull DOMAIN entity);

    /**
     * Deletes an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity to delete; must not be null
     * @throws NotFoundException if no entity exists with the given ID
     */
    void delete(@NonNull ID id);
}
