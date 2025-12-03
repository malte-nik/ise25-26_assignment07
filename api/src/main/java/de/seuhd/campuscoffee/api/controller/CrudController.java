package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.dtos.Dto;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;
import de.seuhd.campuscoffee.domain.ports.api.CrudService;
import de.seuhd.campuscoffee.api.mapper.DtoMapper;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Abstract base controller providing common CRUD operations.
 * Subclasses must implement the abstract methods to provide the service and mapper instances.
 *
 * @param <DOMAIN> the domain object type
 * @param <DTO>    the data transfer object type that must implement Identifiable
 * @param <ID>     the type of the unique identifier (e.g., Long, UUID, String)
 */
public abstract class CrudController<
        DOMAIN extends DomainModel<ID>,
        DTO extends Dto<ID>,
        ID> {

    /**
     * Returns the service instance for CRUD operations, which is used in the methods that
     * the CRUD controller provides. This resembles the template method pattern.
     *
     * @return the CrudService implementation for the domain objects
     */
    protected abstract @NonNull CrudService<DOMAIN, ID> service();

    /**
     * Returns the mapper instance for DTO/Domain conversions, which is used in the methods that
     * the CRUD controller provides. This resembles the template method pattern.
     *
     * @return the DtoMapper implementation for converting between domain objects and DTOs
     */
    protected abstract @NonNull DtoMapper<DOMAIN, DTO> mapper();

    /**
     * Retrieves all resources.
     *
     * @return ResponseEntity containing a list of all resources as DTOs
     */
    protected @NonNull ResponseEntity<List<DTO>> getAll() {
        return ResponseEntity.ok(
                service().getAll().stream()
                        .map(mapper()::fromDomain)
                        .toList()
        );
    }

    /**
     * Retrieves a single resource by ID.
     *
     * @param id the ID of the resource to retrieve
     * @return ResponseEntity containing the resource as a DTO
     */
    protected @NonNull ResponseEntity<DTO> getById(ID id) {
        return ResponseEntity.ok(
                mapper().fromDomain(service().getById(id))
        );
    }

    /**
     * Creates a new resource.
     *
     * @param dto the DTO representing the resource to create
     * @return ResponseEntity with 201 Created status containing the created resource as a DTO
     */
    protected @NonNull ResponseEntity<DTO> create(DTO dto) {
        DTO created = upsert(dto);
        return ResponseEntity
                .created(getLocation(created.getId()))
                .body(created);
    }

    /**
     * Updates an existing resource by ID.
     *
     * @param id the ID of the resource to update
     * @param dto the DTO containing the updated resource data
     * @return ResponseEntity containing the updated resource as a DTO
     * @throws IllegalArgumentException if the ID in the path doesn't match the ID in the DTO
     */
    protected @NonNull ResponseEntity<DTO> update(ID id, DTO dto) {
        if (!id.equals(dto.getId())) {
            throw new IllegalArgumentException("ID in path and body do not match.");
        }
        return ResponseEntity.ok(
                upsert(dto)
        );
    }

    /**
     * Deletes a resource by ID.
     *
     * @param id the ID of the resource to delete
     * @return ResponseEntity with 204 No Content status
     */
    protected @NonNull ResponseEntity<Void> delete(ID id) {
        service().delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Upserts a resource (create if ID is null, update otherwise).
     * Converts DTO to domain, calls the service, and converts back to DTO.
     *
     * @param dto the DTO to upsert
     * @return the upserted entity as a DTO
     */
    protected @NonNull DTO upsert(DTO dto) {
        return mapper().fromDomain(
                service().upsert(
                        mapper().toDomain(dto)
                )
        );
    }

    /**
     * Builds the location URI for a newly created resource.
     * This is required to build the body of a 201 Created response.
     *
     * @param resourceId the ID of the created resource
     * @return the location URI
     */
    protected @NonNull URI getLocation(ID resourceId) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
