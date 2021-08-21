package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcTarget} and its DTO {@link SvcTargetDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcTargetTypeMapper.class, SvcContractMapper.class })
public interface SvcTargetMapper extends EntityMapper<SvcTargetDTO, SvcTarget> {
    @Mapping(target = "type", source = "type", qualifiedByName = "id")
    @Mapping(target = "svcTarget", source = "svcTarget", qualifiedByName = "id")
    SvcTargetDTO toDto(SvcTarget s);

    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcTargetDTO toDtoId(SvcTarget svcTarget);
}
