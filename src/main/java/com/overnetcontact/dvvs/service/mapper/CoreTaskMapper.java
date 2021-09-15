package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoreTask} and its DTO {@link CoreTaskDTO}.
 */
@Mapper(componentModel = "spring", uses = { CoreSuppliesMapper.class })
public interface CoreTaskMapper extends EntityMapper<CoreTaskDTO, CoreTask> {
    @Mapping(target = "coreSupplies", source = "coreSupplies", qualifiedByName = "idSet")
    CoreTaskDTO toDto(CoreTask s);

    @Mapping(target = "removeCoreSupplies", ignore = true)
    CoreTask toEntity(CoreTaskDTO coreTaskDTO);
}
