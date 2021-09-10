package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcSchedulePlanRecord} and its DTO {@link SvcSchedulePlanRecordDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcScheduleUnitRecordMapper.class })
public interface SvcSchedulePlanRecordMapper extends EntityMapper<SvcSchedulePlanRecordDTO, SvcSchedulePlanRecord> {
    @Mapping(target = "svcScheduleUnitRecords", source = "svcScheduleUnitRecords", qualifiedByName = "idSet")
    SvcSchedulePlanRecordDTO toDto(SvcSchedulePlanRecord s);

    @Mapping(target = "removeSvcScheduleUnitRecord", ignore = true)
    SvcSchedulePlanRecord toEntity(SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO);
}
