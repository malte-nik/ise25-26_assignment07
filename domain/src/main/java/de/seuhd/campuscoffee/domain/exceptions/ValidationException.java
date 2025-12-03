package de.seuhd.campuscoffee.domain.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Base exception thrown when an entity fails Bean Validation or violates business rules.
 */
@Getter
public class ValidationException extends RuntimeException {
    private final Set<? extends ConstraintViolation<?>> violations;

    @SuppressWarnings("unused") // will later be used when manually validating objects
    public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        super(formatViolations(violations));
        this.violations = violations;
    }

    /**
     * Creates a validation exception with a custom message for business rule violations.
     *
     * @param message the validation error message
     */
    public ValidationException(String message) {
        super(message);
        this.violations = Set.of();
    }

    /**
     * Formats constraint violations into a readable message.
     */
    private static String formatViolations(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
    }
}
