package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcLabor} and its DTO {@link SvcLaborDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcPlanUnitMapper.class })
public interface SvcLaborMapper extends EntityMapper<SvcLaborDTO, SvcLabor> {
    @Mapping(target = "svcPlanUnit", source = "svcPlanUnit", qualifiedByName = "id")
    SvcLaborDTO toDto(SvcLabor s);
}
