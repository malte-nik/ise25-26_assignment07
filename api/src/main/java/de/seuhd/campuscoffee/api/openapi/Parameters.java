package de.seuhd.campuscoffee.api.openapi;

import lombok.Builder;

import java.util.Optional;

/**
 * Record of parameters extracted from a CrudOperation annotation.
 * Encapsulates all information needed to generate OpenAPI documentation for a CRUD operation.
 *
 * @param operation the type of CRUD operation being performed.
 * @param resource  the resource name being operated on.
 * @param externalResource  optional external resource name for IMPORT operations.
 */
@Builder
public record Parameters(Operation operation, Resource resource, Optional<Resource> externalResource) {
    /**
     * Gets the display form of the resource (singular or plural based on operation).
     *
     * @return The resource name in the appropriate form
     */
    public String getResourceName() {
        return resource.displayNameForOperation(operation);
    }

    /**
     * Gets the display form of the external resource name (singular or plural based on operation).
     *
     * @return The external resource name in the appropriate form
     */
    public Optional<String> getExternalResourceName() {
        return externalResource.map(r -> r.displayNameForOperation(operation));
    }
}

