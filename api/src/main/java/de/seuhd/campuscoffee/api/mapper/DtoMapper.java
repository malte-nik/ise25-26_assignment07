package de.seuhd.campuscoffee.api.mapper;

import de.seuhd.campuscoffee.api.dtos.Dto;
import de.seuhd.campuscoffee.domain.model.objects.DomainModel;

/**
 * Generic mapper interface for converting between domain models and DTOs.
 * This interface defines the standard bidirectional mapping operations between
 * domain entities and their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This is part of the API layer adapter in the hexagonal architecture, enabling the
 * domain layer to remain independent of API concerns.
 *
 * @param <DOMAIN> the domain model type
 * @param <DTO>    the data transfer object type
 */
public interface DtoMapper<
        DOMAIN extends DomainModel<?>,
        DTO extends Dto<?>> {
    /**
     * Converts a domain model object to its DTO representation.
     *
     * @param source the domain model object to convert
     * @return the corresponding DTO
     */
    DTO fromDomain(DOMAIN source);

    /**
     * Converts a DTO to its domain model representation.
     *
     * @param source the DTO to convert
     * @return the corresponding domain model object
     */
    DOMAIN toDomain(DTO source);
}
