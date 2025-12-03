package de.seuhd.campuscoffee.api.openapi;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Specification of a single API response.
 */
@Builder
@Getter
public class CrudResponseSpecification {
    /**
     * HTTP status code (e.g., "200", "404", "409")
     */
    private final HttpStatus httpStatus;

    /**
     * Description template with %s placeholder for resource name.
     * Example: "The %s with the provided ID as a JSON object."
     */
    private final String descriptionTemplate;

    /**
     * Defines whether this is an error response and hence the ErrorResponse schema needs to be returned.
     */
    @Builder.Default
    private final boolean isErrorResponse = false;

    /**
     * Defines whether to use the external resource name instead of the regular resource name in the description.
     */
    @Builder.Default
    private final boolean isExternalResource = false;
}
