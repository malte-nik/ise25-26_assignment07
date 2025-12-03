package de.seuhd.campuscoffee.data.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Maps an entity field to its corresponding domain field.
 * Used by the constraint extraction system to automatically generate
 * field extractors for unique constraint validation.
 *
 * <p>Example usage:
 * <pre>
 * {@literal @}Entity
 * public class PosEntity {
 *     {@literal @}Column(unique = true)
 *     {@literal @}DomainField("name")
 *     private String name;
 * }
 * </pre>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainField {
    /**
     * The name of the corresponding field in the domain class.
     * This should match the accessor method name (e.g., "name" for Pos::name).
     *
     * @return the domain field name
     */
    String value();
}
