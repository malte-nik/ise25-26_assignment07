package de.seuhd.campuscoffee.api.mapper;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.domain.model.objects.Pos;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * MapStruct mapper for converting between {@link Pos} domain model objects and {@link PosDto}s.
 */
@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface PosDtoMapper extends DtoMapper<Pos, PosDto> {
}
