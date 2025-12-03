package de.seuhd.campuscoffee.domain.model.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enum for OpenStreetMap amenity types relevant for CampusCoffee POS.
 * Based on <a href="https://wiki.openstreetmap.org/wiki/Key:amenity">wiki.openstreetmap.org/wiki/Key:amenity</a>
 */
public enum OsmAmenity {
    BAR,
    BIERGARTEN,
    CAFE,
    FAST_FOOD,
    FOOD_COURT,
    ICE_CREAM,
    PUB,
    RESTAURANT,
    VENDING_MACHINE;

    /**
     * Parses an OpenStreetMap amenity string value to its corresponding enum constant.
     *
     * @param osmValue the OSM string value (e.g., "bar", "fast_food")
     * @return an Optional containing the matching enum constant, or empty if no match found
     */
    public static Optional<OsmAmenity> fromOsmValue(String osmValue) {
        return Arrays.stream(values())
                .filter(amenity -> amenity.name().toLowerCase().equals(osmValue))
                .findFirst();
    }
}
