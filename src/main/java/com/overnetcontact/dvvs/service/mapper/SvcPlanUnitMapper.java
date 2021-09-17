package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcPlanUnit} and its DTO {@link SvcPlanUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcPlanMapper.class })
public interface SvcPlanUnitMapper extends EntityMapper<SvcPlanUnitDTO, SvcPlanUnit> {
    @Mapping(target = "svcPlan", source = "svcPlan", qualifiedByName = "id")
    SvcPlanUnitDTO toDto(SvcPlanUnit s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcPlanUnitDTO toDtoId(SvcPlanUnit svcPlanUnit);
}
