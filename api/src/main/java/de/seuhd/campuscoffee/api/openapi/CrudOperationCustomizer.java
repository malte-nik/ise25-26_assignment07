package de.seuhd.campuscoffee.api.openapi;

import de.seuhd.campuscoffee.api.exceptions.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.ResolvableType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.util.Objects;
import java.util.Optional;

import static de.seuhd.campuscoffee.api.openapi.Resource.NONE;

/**
 * Customizes OpenAPI operations based on the @CrudOperation annotation.
 * This component processes the @CrudOperation annotation and populates
 * the OpenAPI operation objects with the appropriate summaries and responses.
 */
@Component
@RequiredArgsConstructor
public class CrudOperationCustomizer implements OperationCustomizer {

    /**
     * Customizes the given OpenAPI operation based on the @CrudOperation annotation present on the handler method.
     *
     * @param operation     the OpenAPI operation to customize
     * @param handlerMethod the Spring HandlerMethod representing the controller method
     * @return the customized OpenAPI operation.
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        CrudOperation crudOperation = handlerMethod.getMethodAnnotation(CrudOperation.class);
        if (crudOperation != null) {
            Parameters params = Parameters.builder()
                .operation(crudOperation.operation())
                .resource(crudOperation.resource())
                .externalResource(Optional.ofNullable(
                        crudOperation.externalResource() == NONE ? null : crudOperation.externalResource())
                )
                .build();
            operation.setSummary(crudOperation.operation().getSummaryTemplate().apply(params));
            operation.setResponses(createResponses(params, handlerMethod));
        }
        return operation;
    }

    /**
     * Creates API responses based on the operation parameters and configuration.
     *
     * @param params Operation parameters including type, resource name, and external resource name
     * @param handlerMethod the controller method to extract the response type from
     * @return configured ApiResponses object
     */
    public ApiResponses createResponses(Parameters params, HandlerMethod handlerMethod) {
        ApiResponses responses = new ApiResponses();
        for (var spec : params.operation().getResponseSpecifications()) {
            ApiResponse response = new ApiResponse().description(formatDescription(spec, params));

            if (spec.isErrorResponse()) {
                response.content(createErrorResponseContent());
            } else {
                response.content(createSuccessResponseContent(handlerMethod));
            }

            responses.addApiResponse(String.valueOf(spec.getHttpStatus().value()), response);
        }

        return responses;
    }

    /**
     * Formats the description template with the appropriate resource name or external resource name.
     *
     * @param spec response specification with description template
     * @param params operation parameters containing resource name and (optionally) the external resource name
     * @return formatted description string
     */
    private String formatDescription(CrudResponseSpecification spec, Parameters params) {
        String substitution;
        if (spec.isExternalResource() && params.getExternalResourceName().isPresent()) {
            // use external resource name
            substitution = params.getExternalResourceName().get();
        } else {
            // use regular resource name
            substitution = params.getResourceName();
        }
        return String.format(spec.getDescriptionTemplate(), substitution);
    }

    /**
     * Creates the content for error responses.
     *
     * @return Content with application/json media type and ErrorResponse schema reference
     */
    private Content createErrorResponseContent() {
        Schema<?> errorSchema = new Schema<>().$ref("#/components/schemas/" + ErrorResponse.class.getSimpleName());
        return new Content()
                .addMediaType("application/json", new MediaType().schema(errorSchema));
    }

    /**
     * Creates the content for successful responses by extracting the DTO type from the method return type.
     *
     * @param handlerMethod the controller method to extract the response type from
     * @return content with application/json media type and DTO schema reference, or null if the return type is Void
     */
    private Content createSuccessResponseContent(HandlerMethod handlerMethod) {
        // get the return type from the method
        ResolvableType returnType = ResolvableType.forMethodReturnType(handlerMethod.getMethod());

        // unwrap ResponseEntity if present
        if (returnType.getRawClass() == ResponseEntity.class) {
            returnType = returnType.getGeneric(0);
        }

        // get the actual class (might be List, DTO, or Void)
        Class<?> responseClass = returnType.getRawClass();
        Objects.requireNonNull(responseClass);

        // if the return type is Void, return null (no content)
        if (responseClass == Void.class || responseClass == void.class) {
            return null;
        }

        Schema<?> schema;
        if (responseClass == java.util.List.class) {
            // extract the generic list type (e.g., UserDto from List<UserDto>)
            Class<?> itemClass = returnType.getGeneric(0).getRawClass();
            Objects.requireNonNull(itemClass);

            // create an array schema with reference to the item type
            schema = new Schema<>();
            schema.type("array");
            Schema<Object> itemSchema = new Schema<>();
            itemSchema.$ref("#/components/schemas/" + itemClass.getSimpleName());
            schema.items(itemSchema);
        } else {
            // single object -> create a direct reference
            schema = new Schema<>().$ref("#/components/schemas/" + responseClass.getSimpleName());
        }

        return new Content()
                .addMediaType("application/json", new MediaType().schema(schema));
    }
}
