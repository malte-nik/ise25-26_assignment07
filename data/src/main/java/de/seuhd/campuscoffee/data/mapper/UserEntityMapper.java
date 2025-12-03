package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.entities.UserEntity;
import de.seuhd.campuscoffee.domain.model.objects.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * MapStruct mapper for converting between domain models and JPA entities.
 * This mapper handles the translation between the {@link User} domain model and the
 * {@link UserEntity} persistence entity.
 * <p>
 * This is part of the data layer adapter in the hexagonal architecture, enabling the
 * domain layer to remain independent of persistence concerns.
 */
@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface UserEntityMapper extends EntityMapper<User, UserEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(User source, @MappingTarget UserEntity target);
}
