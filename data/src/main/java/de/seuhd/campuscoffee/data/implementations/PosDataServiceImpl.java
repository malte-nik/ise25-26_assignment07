package de.seuhd.campuscoffee.data.implementations;

import de.seuhd.campuscoffee.data.mapper.PosEntityMapper;
import de.seuhd.campuscoffee.data.persistence.entities.PosEntity;
import de.seuhd.campuscoffee.data.persistence.repositories.PosRepository;
import de.seuhd.campuscoffee.data.constraints.ConstraintRetriever;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.ports.data.PosDataService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of the POS data service that the domain layer provides as a port.
 * This layer is responsible for data access and persistence.
 * Business logic should be in the service layer.
 * Extends CrudDataServiceImpl to inherit common CRUD operations.
 */
@Service
class PosDataServiceImpl
        extends CrudDataServiceImpl<Pos, PosEntity, PosRepository, Long>
        implements PosDataService {

    /**
     * Constructor that initializes the base CRUD service with POS-specific dependencies.
     *
     * @param repository     the POS repository for data access
     * @param entityMapper   the mapper for converting between POS domain objects and entities
     * @param constraintRetriever the constraint retriever for automatic constraint discovery
     */
    PosDataServiceImpl(PosRepository repository, PosEntityMapper entityMapper, ConstraintRetriever<Pos, PosEntity> constraintRetriever) {
        super(repository, entityMapper, Pos.class, PosEntity.class, constraintRetriever);
    }

    /**
     * Retrieves a POS entity by its unique name and returns it as a domain object.
     *
     * @param name the unique name of the POS to retrieve; must not be null
     * @return the POS with the specified name; never null
     * @throws NotFoundException if no POS exists with the given name
     */
    @Override
    public @NonNull Pos getByName(@NonNull String name) {
        return findByFieldOrThrow(
                () -> repository.findByName(name),
                "name",
                name
        );
    }
}
