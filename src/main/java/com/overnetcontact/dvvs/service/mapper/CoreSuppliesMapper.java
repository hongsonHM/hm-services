package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CoreSupplies} and its DTO {@link CoreSuppliesDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CoreSuppliesMapper extends EntityMapper<CoreSuppliesDTO, CoreSupplies> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CoreSuppliesDTO> toDtoIdSet(Set<CoreSupplies> coreSupplies);
}
