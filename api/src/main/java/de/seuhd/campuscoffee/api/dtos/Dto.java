package de.seuhd.campuscoffee.api.dtos;

import de.seuhd.campuscoffee.domain.model.objects.Identifiable;

/**
 * Marker interface for Data Transfer Objects (DTOs) that have an identifier.
 * This interface extends Identifiable to ensure that all DTOs implementing it
 * provide a method to access their unique identifier.
 */
public interface Dto<ID> extends Identifiable<ID> { }
