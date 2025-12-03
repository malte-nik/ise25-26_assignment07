package de.seuhd.campuscoffee.domain.ports.api;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.exceptions.MissingFieldException;
import de.seuhd.campuscoffee.domain.model.enums.CampusType;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.ports.data.OsmDataService;
import de.seuhd.campuscoffee.domain.ports.data.PosDataService;
import org.jspecify.annotations.NonNull;

/**
 * Service interface for POS (Point of Sale) operations.
 * <p>
 * This is a port in the hexagonal architecture pattern, implemented by the domain layer
 * and consumed by the API layer. It encapsulates business rules and orchestrates
 * data operations through the {@link PosDataService} port.
 * <p>
 * This interface extends {@link CrudService} to inherit common CRUD operations
 * and adds POS-specific operations.
 */
public interface PosService extends CrudService<Pos, Long> {
    /**
     * Retrieves a specific Point of Sale by its unique name.
     *
     * @param name the unique name of the POS to retrieve; must not be null
     * @return the POS entity with the specified name; never null
     * @throws NotFoundException if no POS exists with the given name
     */
    @NonNull Pos getByName(@NonNull String name);

    /**
     * Imports a Point of Sale from an OpenStreetMap node.
     * Fetches POS data from OpenStreetMap using the {@link OsmDataService}, converts it to a POS entity,
     * and saves it to the system. If a POS with the same name already exists, it will be updated.
     * <p>
     * The import process:
     * <ol>
     *   <li>Fetches the OSM node data using the provided node ID</li>
     *   <li>Extracts relevant tags (name, address, etc.)</li>
     *   <li>Maps OSM data to the POS domain model </li>
     *   <li>Persists the POS entity using the upsert method</li>
     * </ol>
     *
     * @param nodeId the OpenStreetMap node ID to import; must not be null
     * @param campusType the campus type to assign to the imported POS; must not be null
     * @return the created or updated POS entity; never null
     * @throws NotFoundException if the OSM node with the given ID doesn't exist or cannot be fetched
     * @throws MissingFieldException if the OSM node lacks required fields for creating a valid POS
     * @throws DuplicationException if a POS with the same name already exists
     */
    @NonNull Pos importFromOsmNode(@NonNull Long nodeId, @NonNull CampusType campusType);
}
