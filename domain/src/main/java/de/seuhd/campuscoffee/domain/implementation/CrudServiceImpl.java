package de.seuhd.campuscoffee.domain.implementation;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;
import de.seuhd.campuscoffee.domain.ports.data.CrudDataService;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Abstract base implementation of CRUD service operations.
 * This class provides common CRUD functionality that can be reused across different services.
 * <p>
 * Subclasses must provide the data service and entity-specific details via abstract methods.
 * This follows the template method pattern.
 *
 * @param <DOMAIN> the domain model type (must implement Identifiable)
 * @param <ID>     the type of the unique identifier (e.g., Long, UUID, String)
 */
@Slf4j
@RequiredArgsConstructor
public abstract class CrudServiceImpl<DOMAIN extends DomainModel<ID>, ID>
        implements CrudService<DOMAIN, ID> {

    /**
     * Returns the data service instance for CRUD operations.
     * Subclasses must provide their specific data service implementation.
     *
     * @return the CrudDataService implementation for the domain type
     */
    protected abstract CrudDataService<DOMAIN, ID> dataService();

    /*
     * The domain class type (used for exception messages).
     */
    protected final Class<DOMAIN> domainClass;

    @Override
    public void clear() {
        log.warn("Clearing all {} data...", domainClass.getSimpleName());
        dataService().clear();
    }

    @Override
    public @NonNull List<DOMAIN> getAll() {
        log.debug("Retrieving all {}...", domainClass.getSimpleName());
        return dataService().getAll();
    }

    @Override
    public @NonNull DOMAIN getById(@NonNull ID id) {
        log.debug("Retrieving {} with ID '{}'...", domainClass.getSimpleName(), id);
        return dataService().getById(id);
    }

    /**
     * Performs the upsert operation with consistent error handling and logging.
     * Database constraint enforces uniqueness - data layer will throw DuplicationException if violated.
     * JPA lifecycle callbacks (@PrePersist/@PreUpdate) set timestamps automatically.
     *
     * @param object the domain object to upsert
     * @return the persisted entity with updated ID and timestamps as a domain object
     * @throws DuplicationException if an entity with duplicate unique fields already exists
     */
    @Override
    @Transactional
    public @NonNull DOMAIN upsert(@NonNull DOMAIN object) {
        ID id = object.getId();

        if (id == null) {
            // Create a new entity
            log.info("Creating new {}...", domainClass.getSimpleName());
        } else {
            // Update an existing entity
            log.info("Updating {} with ID '{}'...", domainClass.getSimpleName(), id);
            // Entity ID must be set
            Objects.requireNonNull(id);
            // Entity must exist in the database before the update
            dataService().getById(id);
        }

        try {
            DOMAIN upsertedEntity = dataService().upsert(object);
            log.info("Successfully upserted {} with ID: '{}'.", domainClass.getSimpleName(), upsertedEntity.getId());
            return upsertedEntity;
        } catch (DuplicationException e) {
            log.error("Error upserting {}: {}", domainClass.getSimpleName(), e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(@NonNull ID id) {
        log.info("Trying to delete {} with ID '{}'...", domainClass.getSimpleName(), id);
        dataService().delete(id);
        log.info("{} with ID {} deleted.", domainClass.getSimpleName(), id);
    }
}
