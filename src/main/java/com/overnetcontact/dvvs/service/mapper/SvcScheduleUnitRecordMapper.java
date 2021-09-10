package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcScheduleUnitRecord} and its DTO {@link SvcScheduleUnitRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcScheduleUnitRecordMapper extends EntityMapper<SvcScheduleUnitRecordDTO, SvcScheduleUnitRecord> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<SvcScheduleUnitRecordDTO> toDtoIdSet(Set<SvcScheduleUnitRecord> svcScheduleUnitRecord);
}
