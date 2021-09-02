package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcContract} and its DTO {@link SvcContractDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { UserMapper.class, SvcUnitMapper.class, OrgUserMapper.class, SvcClientMapper.class, OrgGroupMapper.class }
)
public interface SvcContractMapper extends EntityMapper<SvcContractDTO, SvcContract> {
    @Mapping(target = "approveBy", source = "approveBy", qualifiedByName = "login")
    @Mapping(target = "managerBy", source = "managerBy", qualifiedByName = "login")
    @Mapping(target = "notificationUnits", source = "notificationUnits", qualifiedByName = "id")
    @Mapping(target = "ownerBy", source = "ownerBy", qualifiedByName = "id")
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "saler", source = "saler", qualifiedByName = "id")
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    SvcContractDTO toDto(SvcContract s);

    @Named("id")
    // @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcContractDTO toDtoId(SvcContract svcContract);
}
