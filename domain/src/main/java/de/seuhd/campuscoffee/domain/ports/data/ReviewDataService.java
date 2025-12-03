package de.seuhd.campuscoffee.domain.ports.data;

import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.model.objects.User;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Data service interface for review persistence operations.
 * <p>
 * This is a port in the hexagonal architecture pattern, defined by the domain layer
 * and implemented by the data layer. It provides data access operations for reviews.
 * <p>
 * This interface extends {@link CrudDataService} to inherit common CRUD operations
 * and adds review-specific operations.
 */
public interface ReviewDataService extends CrudDataService<Review, Long> {
    /**
     * Retrieves all reviews for a specific point of sale that are approved/unapproved.
     *
     * @param pos the point of sale to retrieve reviews for
     * @param approved the approval status to filter by
     * @return a list of all reviews for the specified point of sale
     */
    @NonNull List<Review> filter(@NonNull Pos pos, @NonNull Boolean approved);

    /**
     * Retrieves all reviews for a specific point of sale authored by a specific user.
     *
     * @param pos       the point of sale to retrieve reviews for
     * @param author    the author whose reviews to retrieve
     * @return          a list of reviews for the specified point of sale and author
     */
    @NonNull List<Review> filter(@NonNull Pos pos, @NonNull User author);
}
