package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcPlan} and its DTO {@link SvcPlanDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcPlanMapper extends EntityMapper<SvcPlanDTO, SvcPlan> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcPlanDTO toDtoId(SvcPlan svcPlan);
}
