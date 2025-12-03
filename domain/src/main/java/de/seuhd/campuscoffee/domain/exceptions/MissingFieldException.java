package de.seuhd.campuscoffee.domain.exceptions;

import de.seuhd.campuscoffee.domain.model.objects.DomainModel;

/**
 * Generic exception thrown when an entity is missing a required field.
 * This represents a business rule violation: certain fields are mandatory.
 */
public class MissingFieldException extends RuntimeException {
    /**
     * Create missing field exception from domain class, object ID, and field name.
     *
     * @param <DOMAIN> domain type
     * @param <ID> ID type
     * @param domainClass class of domain object (e.g., "Pos", "User")
     * @param id unique identifier of the domain object with a missing field
     * @param fieldName name of the missing field
     */
    public <DOMAIN extends DomainModel<ID>, ID> MissingFieldException(Class<DOMAIN> domainClass, ID id, String fieldName) {
        super(domainClass.getSimpleName() + " with ID " + id + " does not have the required fields. " +
                "Field '" + fieldName + "' is missing.");
    }
}
