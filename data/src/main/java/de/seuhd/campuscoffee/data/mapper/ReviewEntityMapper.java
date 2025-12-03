package de.seuhd.campuscoffee.data.mapper;

import de.seuhd.campuscoffee.data.persistence.entities.ReviewEntity;
import de.seuhd.campuscoffee.domain.model.objects.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Mapper(componentModel = "spring",
        uses = {PosEntityMapper.class, UserEntityMapper.class})
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface ReviewEntityMapper extends EntityMapper<Review, ReviewEntity> {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "approvalCount", defaultValue = "0")
    void updateEntity(Review source, @MappingTarget ReviewEntity target);
}
