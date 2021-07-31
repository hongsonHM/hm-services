package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcClient} and its DTO {@link SvcClientDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcClientMapper extends EntityMapper<SvcClientDTO, SvcClient> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcClientDTO toDtoId(SvcClient svcClient);
}
