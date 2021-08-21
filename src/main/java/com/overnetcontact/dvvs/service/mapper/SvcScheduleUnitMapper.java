package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcScheduleUnit} and its DTO {@link SvcScheduleUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcScheduleUnitMapper extends EntityMapper<SvcScheduleUnitDTO, SvcScheduleUnit> {
    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcScheduleUnitDTO toDtoId(SvcScheduleUnit svcScheduleUnit);
}
