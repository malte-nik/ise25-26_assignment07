package de.seuhd.campuscoffee.data.constraints;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Represents a unique field constraint mapping.
 * Associates a database column with its constraint name and the domain field name
 * for extracting the field value from a domain object.
 * <p>
 * This record is used by the constraint extraction system to provide meaningful
 * error messages when unique constraint violations occur.
 *
 * @param <DOMAIN>        the domain model type that contains the unique field
 * @param domainFieldName the name of the accessor method in the domain record (e.g., "name" for Pos::name)
 * @param columnName      the name of the database column for the unique field
 * @param constraintName  the name of the unique constraint in the database
 */
public record ConstraintMapping<DOMAIN>(
        String domainFieldName,
        String columnName,
        String constraintName
) {
    /**
     * Extracts the field value from a domain object using reflection.
     * This method is called when a constraint violation occurs to include
     * the actual value in the error message.
     *
     * @param domain the domain object to extract the value from
     * @return the field value
     */
    public Object extractValue(DOMAIN domain) {
        Objects.requireNonNull(domain);
        try {
            return domain.getClass()
                    .getMethod(domainFieldName)
                    .invoke(domain);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
