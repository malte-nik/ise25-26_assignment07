package de.seuhd.campuscoffee.api.openapi;

import de.seuhd.campuscoffee.api.exceptions.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for the CampusCoffee API.
 * Defines global API metadata such as title, version, and description.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "CampusCoffee API",
                version = "0.0.4",
                description = "REST API for managing campus coffee points of sale, users, and reviews."
        )
)
public class OpenApiConfig {
    /**
     * Registers the ErrorResponse schema in the OpenAPI components section.
     * This is required because the ErrorResponse is not explicitly referenced in the controller but only used
     * programmatically as part of our custom OpenAPI annotations.
     * This ensures that ErrorResponse appears in the Swagger UI schemas list
     * and can be referenced by error responses throughout the API.
     *
     * @return OpenApiCustomizer that adds ErrorResponse to components/schemas
     */
    @Bean
    public OpenApiCustomizer errorResponseSchemaCustomizer() {
        return openApi -> {
            var schemas = ModelConverters.getInstance().read(ErrorResponse.class);
            schemas.forEach((name, schema) ->
                    openApi.getComponents().addSchemas(name, schema)
            );
        };
    }
}
