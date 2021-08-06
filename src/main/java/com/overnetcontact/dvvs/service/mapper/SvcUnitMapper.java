package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcUnit} and its DTO {@link SvcUnitDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcGroupMapper.class })
public interface SvcUnitMapper extends EntityMapper<SvcUnitDTO, SvcUnit> {
    @Mapping(target = "group", source = "group", qualifiedByName = "id")
    SvcUnitDTO toDto(SvcUnit s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcUnitDTO toDtoId(SvcUnit svcUnit);
}
