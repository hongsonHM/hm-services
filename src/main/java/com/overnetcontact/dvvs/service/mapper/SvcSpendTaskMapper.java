package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcSpendTask} and its DTO {@link SvcSpendTaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcGroupTaskMapper.class })
public interface SvcSpendTaskMapper extends EntityMapper<SvcSpendTaskDTO, SvcSpendTask> {
    @Mapping(target = "svcGroupTask", source = "svcGroupTask", qualifiedByName = "id")
    SvcSpendTaskDTO toDto(SvcSpendTask s);
}
