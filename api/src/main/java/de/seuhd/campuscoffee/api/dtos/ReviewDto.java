package de.seuhd.campuscoffee.api.dtos;

import lombok.Builder;
import org.jspecify.annotations.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import java.time.LocalDateTime;

/**
 * DTO record for POS metadata.
 */
@Builder(toBuilder = true)
public record ReviewDto (
        @Nullable Long id,
        @Nullable LocalDateTime createdAt, // is null when using DTO to create a new POS
        @Nullable LocalDateTime updatedAt, // is set when creating or updating a POS

        @NotNull
        @NonNull Long posId,

        @NotNull
        @NonNull Long authorId,

        @NotBlank(message = "the review must be filled.")
        @NonNull String review,

        @Nullable Boolean approved
) implements Dto<Long> {
    @Override
    public @Nullable Long getId() {
        return id;
    }
}
