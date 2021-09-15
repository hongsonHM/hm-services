package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcArea} and its DTO {@link SvcAreaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcAreaMapper extends EntityMapper<SvcAreaDTO, SvcArea> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcAreaDTO toDtoId(SvcArea svcArea);
}
