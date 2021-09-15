package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcGroupTask} and its DTO {@link SvcGroupTaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcAreaMapper.class })
public interface SvcGroupTaskMapper extends EntityMapper<SvcGroupTaskDTO, SvcGroupTask> {
    @Mapping(target = "svcArea", source = "svcArea", qualifiedByName = "id")
    SvcGroupTaskDTO toDto(SvcGroupTask s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcGroupTaskDTO toDtoId(SvcGroupTask svcGroupTask);
}
