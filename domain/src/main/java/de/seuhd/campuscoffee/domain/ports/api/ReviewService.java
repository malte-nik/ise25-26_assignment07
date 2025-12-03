package de.seuhd.campuscoffee.domain.ports.api;

import de.seuhd.campuscoffee.domain.model.objects.Review;
import de.seuhd.campuscoffee.domain.ports.data.ReviewDataService;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Service interface for review operations.
 *  * <p>
 *  * This is a port in the hexagonal architecture pattern, implemented by the domain layer
 *  * and consumed by the API layer. It encapsulates business rules and orchestrates
 *  * data operations through the {@link ReviewDataService} port.
 *  * <p>
 *  * This interface extends {@link CrudService} to inherit common CRUD operations
 *  * and adds review-specific operations.
 */
public interface ReviewService extends CrudService<Review, Long> {
    /**
     * Filters reviews by point of sale and approval status.
     *
     * @param posId      unique identifier of the point of sale to filter reviews for
     * @param approved the approval status to filter by
     * @return a list of reviews matching the filter criteria
     */
    @NonNull List<Review> filter(@NonNull Long posId, @NonNull Boolean approved);

    /**
     * Approves a review on behalf of a user.
     * The approval count is incremented, and the review may be marked as approved
     * if the approval threshold is reached.
     *
     * @param review    the review to approve
     * @param userId    unique identifier of the user approving the review
     * @return the updated review with incremented approval count
     */
    @NonNull Review approve(@NonNull Review review, @NonNull Long userId);
}
