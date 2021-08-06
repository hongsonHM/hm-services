package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcSchedulePlan} and its DTO {@link SvcSchedulePlanDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrgUserMapper.class, SvcContractMapper.class, SvcScheduleUnitMapper.class })
public interface SvcSchedulePlanMapper extends EntityMapper<SvcSchedulePlanDTO, SvcSchedulePlan> {
    @Mapping(target = "serviceManager", source = "serviceManager", qualifiedByName = "id")
    @Mapping(target = "defaultSupervisor", source = "defaultSupervisor", qualifiedByName = "id")
    @Mapping(target = "contract", source = "contract", qualifiedByName = "id")
    @Mapping(target = "scheduleUnit", source = "scheduleUnit", qualifiedByName = "id")
    SvcSchedulePlanDTO toDto(SvcSchedulePlan s);
}
