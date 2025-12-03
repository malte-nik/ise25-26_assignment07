package de.seuhd.campuscoffee.api.dtos;

import de.seuhd.campuscoffee.domain.model.enums.CampusType;
import de.seuhd.campuscoffee.domain.model.enums.PosType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * DTO record for POS metadata.
 */
@Builder(toBuilder = true)
public record PosDto (
        @Nullable Long id, // id is null when creating a new task
        @Nullable LocalDateTime createdAt, // is null when using DTO to create a new POS
        @Nullable LocalDateTime updatedAt, // is set when creating or updating a POS

        @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters long.")
        @NonNull String name,

        @NotBlank(message = "Description cannot be empty.")
        @NonNull String description,

        @NotNull
        @NonNull PosType type,

        @NotNull
        @NonNull CampusType campus,

        @NotBlank(message = "Street cannot be empty.")
        @NonNull String street,

        @NotNull
        @Size(min = 1, max = 255, message = "House number must be between 1 and 255 characters long.")
        @NonNull String houseNumber,

        @NotNull
        @NonNull Integer postalCode,

        @NotNull
        @Size(min = 1, max = 255, message = "City must be between 1 and 255 characters long.")
        @NonNull String city
) implements Dto<Long> {
    @Override
    public Long getId() {
        return id;
    }
}
