package de.seuhd.campuscoffee.domain.model.objects;

import de.seuhd.campuscoffee.domain.model.enums.OsmAmenity;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

/**
 * Represents an OpenStreetMap node with relevant Point of Sale information.
 * This is the domain model for OSM data before it is converted to a POS object.
 *
 * @param nodeId      the OpenStreetMap node ID
 * @param city        the city where the node is located
 * @param houseNumber the house number of the node's address
 * @param postcode    the postal code of the node's address
 * @param street      the street name of the node's address
 * @param amenity     the type of amenity this node represents
 * @param name        the name of the OSM node
 * @param description a description of the ODM node
 */
@Builder
public record OsmNode(
        @NonNull Long nodeId,
        @NonNull String city,
        @NonNull String houseNumber,
        @NonNull String postcode,
        @NonNull String street,
        @NonNull OsmAmenity amenity,
        @NonNull String name,
        @NonNull String description
) implements DomainModel<Long> {
    @Override
    public Long getId() {
        return nodeId;
    }
}
