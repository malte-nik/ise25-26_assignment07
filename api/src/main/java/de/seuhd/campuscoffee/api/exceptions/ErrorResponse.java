package de.seuhd.campuscoffee.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

import java.time.LocalDateTime;

/**
 * Standardized error response structure for all API exceptions.
 * Provides consistent error information to API consumers.
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // excludes null fields from JSON
public record ErrorResponse (
    /*
     * Machine-readable error code based on the exception class name (e.g., NotFoundException).
     * Enables clients to handle specific error types programmatically.
     */
    @NonNull String errorCode,
    /*
     * Human-readable error message.
     */
    @NonNull String message,
    /*
     * HTTP status code (e.g., 400, 404, 500).
     */
    @NonNull Integer statusCode,
    /*
     * HTTP status message (e.g., "Bad Request", "Not Found").
     */
    String statusMessage,
    /*
     * Timestamp when the error occurred.
     */
    LocalDateTime timestamp,
    /*
     * Request path that caused the error.
     */
    String path
) { }
