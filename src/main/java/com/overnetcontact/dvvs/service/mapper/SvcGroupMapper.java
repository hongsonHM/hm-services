package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcGroup} and its DTO {@link SvcGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcGroupMapper extends EntityMapper<SvcGroupDTO, SvcGroup> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcGroupDTO toDtoId(SvcGroup svcGroup);
}
