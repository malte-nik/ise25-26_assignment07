package de.seuhd.campuscoffee.api.openapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Unified annotation for all CRUD operations.
 * Automatically generates complete OpenAPI documentation including summary and responses.
 * <p>
 * Example usage:
 * <pre>
 * {@code @CrudOperation(type = OperationType.GET_ALL, resource = ResourceName.USER)}
 * {@code @GetMapping("")}
 * public ResponseEntity&lt;List&lt;UserDto&gt;&gt; getAll() { ... }
 *
 * {@code @CrudOperation(type = OperationType.IMPORT, resource = ResourceName.POS, externalResource = ResourceName.OSM_NODE)}
 * {@code @PostMapping("/import/osm/{nodeId}")}
 * public ResponseEntity&lt;PosDto&gt; importFromOsm(...) { ... }
 * </pre>
 *
 * @see de.seuhd.campuscoffee.api.openapi.CrudOperationCustomizer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrudOperation {
    /**
     * The type of CRUD operation being performed.
     *
     * @return The operation type
     */
    Operation operation();

    /**
     * Resource identifier using a type-safe enum.
     * The enum automatically handles singular/plural forms based on the operation type.
     *
     * @return The resource name
     */
    Resource resource();

    /**
     * Optional external identifier for IMPORT operations (e.g., "OpenStreetMap node").
     *
     * @return The external resource name or empty string if not specified
     */
    Resource externalResource() default Resource.NONE;
}
