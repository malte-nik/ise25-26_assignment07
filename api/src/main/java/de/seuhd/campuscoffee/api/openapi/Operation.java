package de.seuhd.campuscoffee.api.openapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.function.Function;

/**
 * Defines all supported CRUD operation types with their configuration.
 */
@RequiredArgsConstructor
@Getter
public enum Operation {
    GET_ALL(
            (params) -> "Get all " + params.getResourceName() + ".",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.OK)
                            .descriptionTemplate("All %s as a JSON array.")
                            .build()
            )
    ),
    GET_BY_ID(
            (params) -> "Get " + params.getResourceName() + " by ID.",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.OK)
                            .descriptionTemplate("The %s with the provided ID as a JSON object.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .descriptionTemplate("No %s with the provided ID could be found.")
                            .isErrorResponse(true)
                            .build()
            )
    ),
    CREATE(
            (params) -> "Create a new " + params.getResourceName() + ".",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.CREATED)
                            .descriptionTemplate("The new %s as a JSON object.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .descriptionTemplate("Validation failed (e.g., bean validation error).")
                            .isErrorResponse(true)
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.CONFLICT)
                            .descriptionTemplate("A %s with the same value for a unique field already exists.")
                            .isErrorResponse(true)
                            .build()
            )
    ),
    UPDATE(
            (params) -> "Update " + params.getResourceName() + " by ID.",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.OK)
                            .descriptionTemplate("The updated %s as a JSON object.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .descriptionTemplate("Validation failed (e.g., IDs in path and body do not match, bean validation error).")
                            .isErrorResponse(true)
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .descriptionTemplate("No %s with the provided ID could be found.")
                            .isErrorResponse(true)
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.CONFLICT)
                            .descriptionTemplate("A %s with the same unique identifier already exists.")
                            .isErrorResponse(true)
                            .build()
            )
    ),
    DELETE(
            (params) -> "Delete " + params.getResourceName() + " by ID.",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NO_CONTENT)
                            .descriptionTemplate("The %s was successfully deleted.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .descriptionTemplate("No %s with the provided ID could be found.")
                            .isErrorResponse(true)
                            .build()
            )
    ),
    FILTER(
            (params) -> "Filter " + params.getResourceName() + " by a selected field.",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.OK)
                            .descriptionTemplate("The %s matching the filter criteria as a JSON object.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .descriptionTemplate("No %s matching the filter criteria could be found.")
                            .isErrorResponse(true)
                            .build()
            )
    ),
    IMPORT(
            (params) -> "Import " + params.getResourceName() + " from " + params.getExternalResourceName()
                    .orElseThrow(() -> new IllegalArgumentException("External resource name not set.")) + ".",
            List.of(
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.CREATED)
                            .descriptionTemplate("The imported %s as a JSON object.")
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .descriptionTemplate("Validation failed or the external %s is invalid.")
                            .isErrorResponse(true)
                            .isExternalResource(true)
                            .build(),
                    CrudResponseSpecification.builder()
                            .httpStatus(HttpStatus.NOT_FOUND)
                            .descriptionTemplate("The external %s could not be found.")
                            .isErrorResponse(true)
                            .isExternalResource(true)
                            .build()
            )
    );

    /**
     * Function to generate the OpenAPI summary from operation parameters.
     */
    private final Function<Parameters, String> summaryTemplate;

    /**
     * Response specifications defining response codes, descriptions, and potential error responses.
     */
    private final List<CrudResponseSpecification> responseSpecifications;
}

