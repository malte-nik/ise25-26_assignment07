package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.entities.Entity;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;
import org.mapstruct.MappingTarget;

/**
 * Generic mapper interface for converting between domain models and JPA entities.
 * This interface defines the standard bidirectional mapping operations between
 * domain objects and their corresponding persistence entities.
 * <p>
 * This is part of the data layer adapter in the hexagonal architecture, enabling the
 * domain layer to remain independent of persistence concerns.
 *
 * @param <DOMAIN> the domain model type
 * @param <ENTITY> the JPA entity type
 */
public interface EntityMapper<
        DOMAIN extends DomainModel<?>,
        ENTITY extends Entity> {

    /**
     * Converts a JPA entity to its domain model representation.
     *
     * @param source the JPA entity to convert
     * @return the corresponding domain model object
     */
    DOMAIN fromEntity(ENTITY source);

    /**
     * Converts a domain model object to its JPA entity representation.
     *
     * @param source the domain model object to convert
     * @return the corresponding JPA entity
     */
    ENTITY toEntity(DOMAIN source);

    /**
     * Updates an existing JPA entity with data from the domain model.
     * This method is intended for update operations where the entity already exists.
     * JPA-managed fields (id, createdAt, updatedAt) should be preserved and not overwritten.
     *
     * @param source the domain model containing the new data; must not be null
     * @param target the existing JPA entity to update; must not be null
     */
    void updateEntity(DOMAIN source, @MappingTarget ENTITY target);
}
