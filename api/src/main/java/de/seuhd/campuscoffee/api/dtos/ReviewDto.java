package de.seuhd.campuscoffee.api.dtos;

import lombok.Builder;
import org.jspecify.annotations.Nullable;

/**
 * DTO record for POS metadata.
 */
@Builder(toBuilder = true)
public record ReviewDto (
    @Nullable Long id
    // TODO: Implement ReviewDto
) implements Dto<Long> {
    @Override
    public @Nullable Long getId() {
        return id;
    }
}
