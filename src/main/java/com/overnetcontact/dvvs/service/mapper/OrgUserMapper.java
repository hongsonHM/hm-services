package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.OrgUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrgUser} and its DTO {@link OrgUserDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, OrgGroupMapper.class })
public interface OrgUserMapper extends EntityMapper<OrgUserDTO, OrgUser> {
    @Mapping(target = "internalUser", source = "internalUser")
    @Mapping(target = "group", source = "group", qualifiedByName = "id")
    OrgUserDTO toDto(OrgUser s);

    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrgUserDTO toDtoId(OrgUser orgUser);
}
