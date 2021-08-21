package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrgGroup} and its DTO {@link OrgGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrgGroupMapper extends EntityMapper<OrgGroupDTO, OrgGroup> {
    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrgGroupDTO toDtoId(OrgGroup orgGroup);
}
