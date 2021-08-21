package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcTargetType} and its DTO {@link SvcTargetTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcTargetTypeMapper extends EntityMapper<SvcTargetTypeDTO, SvcTargetType> {
    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcTargetTypeDTO toDtoId(SvcTargetType svcTargetType);
}
