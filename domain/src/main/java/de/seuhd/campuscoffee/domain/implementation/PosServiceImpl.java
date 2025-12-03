package de.seuhd.campuscoffee.domain.implementation;

import de.seuhd.campuscoffee.domain.exceptions.MissingFieldException;
import de.seuhd.campuscoffee.domain.model.enums.CampusType;
import de.seuhd.campuscoffee.domain.model.enums.OsmAmenity;
import de.seuhd.campuscoffee.domain.model.objects.OsmNode;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.model.enums.PosType;
import de.seuhd.campuscoffee.domain.ports.data.CrudDataService;
import de.seuhd.campuscoffee.domain.ports.data.OsmDataService;
import de.seuhd.campuscoffee.domain.ports.data.PosDataService;
import de.seuhd.campuscoffee.domain.ports.api.PosService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the POS service that handles business logic related to POS entities.
 */
@Slf4j
@Service
public class PosServiceImpl extends CrudServiceImpl<Pos, Long> implements PosService {
    private final PosDataService posDataService;
    private final OsmDataService osmDataService;

    PosServiceImpl(@NonNull PosDataService posDataService, @NonNull OsmDataService osmDataService) {
        super(Pos.class);
        this.posDataService = posDataService;
        this.osmDataService = osmDataService;
    }

    @Override
    protected CrudDataService<Pos, Long> dataService() {
        return posDataService;
    }

    @Override
    public @NonNull Pos getByName(@NonNull String name) {
        log.debug("Retrieving POS with name: {}", name);
        return posDataService.getByName(name);
    }

    @Override
    @Transactional
    public @NonNull Pos importFromOsmNode(@NonNull Long nodeId, @NonNull CampusType campusType) {
        log.info("Importing POS from OpenStreetMap node {}...", nodeId);

        // Fetch the OSM node data using the port
        OsmNode osmNode = osmDataService.fetchNode(nodeId);

        // Convert OSM node to POS domain object and upsert it
        Pos savedPos = upsert(convertOsmNodeToPos(osmNode, campusType));
        log.info("Successfully imported POS '{}' from OSM node {}", savedPos.name(), nodeId);

        return savedPos;
    }

    /**
     * Converts an OSM node to a POS domain object.
     * Maps OSM amenity types to POS types and validates required fields.
     *
     * @param osmNode the OSM node data
     * @param campusType the campus where the POS is located
     * @return a new Pos object with data from the OSM node
     * @throws MissingFieldException if required fields are missing or invalid
     */
    private @NonNull Pos convertOsmNodeToPos(@NonNull OsmNode osmNode, @NonNull CampusType campusType) {
        // map OSM amenity to POS type
        PosType posType = mapAmenityToPosType(osmNode.amenity());

        // parse postal code from string to integer
        int postalCode;
        try {
            postalCode = Integer.parseInt(osmNode.postcode());
        } catch (NumberFormatException e) {
            log.error("Could not parse postcode {} of OSM node {}", osmNode.postcode(), osmNode.nodeId());
            throw new MissingFieldException(OsmNode.class, osmNode.nodeId(), "postcode");
        }

        // build and return POS object
        return Pos.builder()
                .name(osmNode.name())
                .description(osmNode.description())
                .type(posType)
                .campus(campusType)
                .street(osmNode.street())
                .houseNumber(osmNode.houseNumber())
                .postalCode(postalCode)
                .city(osmNode.city())
                .build();
    }

    /**
     * Maps an OpenStreetMap amenity type to a POS type.
     *
     * @param amenity the OSM amenity type
     * @return the corresponding POS type
     */
    private PosType mapAmenityToPosType(OsmAmenity amenity) {
        return switch (amenity) {
            case CAFE, ICE_CREAM -> PosType.CAFE;
            case VENDING_MACHINE -> PosType.VENDING_MACHINE;
            case FOOD_COURT -> PosType.CAFETERIA;
            case BAR, BIERGARTEN, PUB, RESTAURANT, FAST_FOOD -> PosType.OTHER;
        };
    }
}
