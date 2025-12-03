package de.seuhd.campuscoffee.domain.ports.api;

import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.model.objects.User;
import de.seuhd.campuscoffee.domain.ports.data.UserDataService;
import org.jspecify.annotations.NonNull;

/**
 * Service interface for user operations.
 * <p>
 * This is a port in the hexagonal architecture pattern, implemented by the domain layer
 * and consumed by the API layer. It encapsulates business rules and orchestrates
 * data operations through the {@link UserDataService} port.
 * <p>
 * This interface extends {@link CrudService} to inherit common CRUD operations
 * and adds user-specific operations.
 */
public interface UserService extends CrudService<User, Long> {
    /**
     * Retrieves a specific user by their unique login name.
     *
     * @param loginName the unique login name of the user to retrieve; must not be null
     * @return the user entity with the specified login name; never null
     * @throws NotFoundException if no user exists with the given login name
     */
    @NonNull User getByLoginName(@NonNull String loginName);
}
