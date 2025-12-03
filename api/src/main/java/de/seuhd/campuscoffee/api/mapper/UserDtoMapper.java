package de.seuhd.campuscoffee.api.mapper;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.objects.User;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * MapStruct mapper for converting between the {@link User} domain model objects and {@link UserDto}s.
 * .
 */
@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface UserDtoMapper extends DtoMapper<User, UserDto> {
}
