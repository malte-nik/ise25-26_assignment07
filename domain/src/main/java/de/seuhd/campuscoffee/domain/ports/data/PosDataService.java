package de.seuhd.campuscoffee.domain.ports.data;

import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import org.jspecify.annotations.NonNull;

/**
 * Port interface for POS data operations.
 * <p>
 * This port is implemented by the data layer (adapter) and defines the contract
 * for persistence operations on Point of Sale entities.
 * Follows the hexagonal architecture pattern where the domain defines the port
 * and the data layer provides the adapter implementation.
 * <p>
 * Extends the generic {@link CrudDataService} to inherit common CRUD operations.
 */
public interface PosDataService extends CrudDataService<Pos, Long> {
    /**
     * Retrieves a single POS entity by its unique name and returns it as a domain object.
     *
     * @param name the name of the POS to retrieve; must not be null
     * @return the POS entity with the specified name; never null
     * @throws NotFoundException if no POS exists with the given name
     */
    @NonNull Pos getByName(@NonNull String name);
}
