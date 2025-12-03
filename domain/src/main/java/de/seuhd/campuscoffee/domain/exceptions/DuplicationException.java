package de.seuhd.campuscoffee.domain.exceptions;

import de.seuhd.campuscoffee.domain.model.objects.DomainModel;

/**
 * Generic exception thrown when attempting to create or update an entity with a value that already exists.
 * This represents a business rule violation: certain fields must be unique.
 */
public class DuplicationException extends RuntimeException {

    /**
     * Creates an exception for a duplicate entity field.
     *
     * @param <DOMAIN> domain type
     * @param domainClass class of domain object (e.g., "Pos", "User")
     * @param fieldName  the field name that must be unique (e.g., "name", "login name", "email address")
     * @param fieldValue the duplicate value
     */
    public <DOMAIN extends DomainModel<?>> DuplicationException(Class<DOMAIN> domainClass, String fieldName, String fieldValue) {
        super(domainClass.getSimpleName() + " with " + fieldName + " '" + fieldValue + "' already exists.");
    }
}
