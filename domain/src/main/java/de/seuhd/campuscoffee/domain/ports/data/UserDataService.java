package de.seuhd.campuscoffee.domain.ports.data;

import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.model.objects.User;
import org.jspecify.annotations.NonNull;

/**
 * Port interface for user data operations.
 * <p>
 * This port is implemented by the data layer (adapter) and defines the contract
 * for persistence operations on user entities.
 * Follows the hexagonal architecture pattern where the domain defines the port
 * and the data layer provides the adapter implementation.
 * <p>
 * Extends the generic {@link CrudDataService} to inherit common CRUD operations.

 */
public interface UserDataService extends CrudDataService<User, Long> {
    /**
     * Retrieves a single user entity by its unique login name and returns it as a domain object.
     *
     * @param loginName the login name of the user to retrieve; must not be null
     * @return the user with the specified login name; never null
     * @throws NotFoundException if no user exists with the given login name
     */
    @NonNull User getByLoginName(@NonNull String loginName);
}
