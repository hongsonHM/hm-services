package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcPlanTask} and its DTO {@link SvcPlanTaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcPlanUnitMapper.class })
public interface SvcPlanTaskMapper extends EntityMapper<SvcPlanTaskDTO, SvcPlanTask> {
    @Mapping(target = "svcPlanUnit", source = "svcPlanUnit", qualifiedByName = "id")
    SvcPlanTaskDTO toDto(SvcPlanTask s);
}
