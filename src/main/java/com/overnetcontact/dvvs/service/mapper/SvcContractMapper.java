package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcContract} and its DTO {@link SvcContractDTO}.
 */
@Mapper(componentModel = "spring", uses = { SvcUnitMapper.class, OrgUserMapper.class, SvcClientMapper.class })
public interface SvcContractMapper extends EntityMapper<SvcContractDTO, SvcContract> {
    @Mapping(target = "unit", source = "unit", qualifiedByName = "id")
    @Mapping(target = "saler", source = "saler", qualifiedByName = "id")
    @Mapping(target = "client", source = "client", qualifiedByName = "id")
    SvcContractDTO toDto(SvcContract s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SvcContractDTO toDtoId(SvcContract svcContract);
}
