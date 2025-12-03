package de.seuhd.campuscoffee.api.openapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Type-safe enum defining all available resources with their singular and plural forms.
 * Provides automatic singular/plural resolution based on the operation type.
 */
@RequiredArgsConstructor
@Getter
public enum Resource {
    /**
     * No resource specified.
     */
    NONE("", ""),

    /**
     * User resource name.
     */
    USER("user", "users"),

    /**
     * Point of Sale (POS) resource name.
     * Uses the same form for both singular and plural.
     */
    POS("POS", "POS"),

    /**
     * Review resource name.
     */
    REVIEW("review", "reviews"),

    /**
     * OpenStreetMap external resource name.
     */
    OSM_NODE("OpenStreetMap node", "OpenStreetMap nodes");

    private final String singular;
    private final String plural;

    /**
     * Returns the appropriate form (singular or plural) for the provided operation type.
     * GET_ALL operations use plural form, all others use singular.
     *
     * @param operation The type of CRUD operation
     * @return the appropriate resource name form
     */
    public String displayNameForOperation(Operation operation) {
        return operation == Operation.GET_ALL ? plural : singular;
    }
}
