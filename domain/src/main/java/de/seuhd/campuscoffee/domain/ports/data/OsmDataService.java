package de.seuhd.campuscoffee.domain.ports.data;

import de.seuhd.campuscoffee.domain.model.objects.OsmNode;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import org.jspecify.annotations.NonNull;

/**
 * Port for importing Point of Sale data from OpenStreetMap.
 * <p>
 * This interface defines the contract for fetching OSM node data.
 * Its implementations handle the external API communication.
 */
public interface OsmDataService {
    /**
     * Fetches an OpenStreetMap node by its ID.
     *
     * @param nodeId the OpenStreetMap node ID to fetch
     * @return the OSM node data with tags
     * @throws NotFoundException if the node doesn't exist or can't be fetched
     */
    @NonNull OsmNode fetchNode(@NonNull Long nodeId);
}
