package de.seuhd.campuscoffee.domain.exceptions;

import de.seuhd.campuscoffee.domain.model.objects.DomainModel;

/**
 * Generic exception thrown when an entity is not found in the database.
 * Supports finding by ID or by a specific field name and value.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Creates an exception for an entity not found by ID.
     *
     * @param <DOMAIN> domain type
     * @param <ID> ID type
     * @param domainClass class of domain object (e.g., "Pos", "User")
     * @param id         the ID that was not found
     */
    public <DOMAIN extends DomainModel<ID>, ID> NotFoundException(Class<DOMAIN> domainClass, ID id) {
        super(domainClass.getSimpleName() + " with ID " + id + " does not exist.");
    }

    /**
     * Creates an exception for an entity not found by a specific field.
     *
     * @param <DOMAIN> domain type
     * @param domainClass class of domain object (e.g., "Pos", "User")
     * @param fieldName  the field name (e.g., "name", "login name")
     * @param fieldValue the field value that was not found
     */
    public <DOMAIN extends DomainModel<?>> NotFoundException(Class<DOMAIN> domainClass, String fieldName, String fieldValue) {
        super(domainClass.getSimpleName() + " with " + fieldName + " '" + fieldValue + "' does not exist.");
    }
}
