package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SvcPlanPart} and its DTO {@link SvcPlanPartDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SvcPlanPartMapper extends EntityMapper<SvcPlanPartDTO, SvcPlanPart> {}
