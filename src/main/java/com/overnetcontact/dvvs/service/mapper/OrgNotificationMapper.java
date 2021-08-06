package com.overnetcontact.dvvs.service.mapper;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrgNotification} and its DTO {@link OrgNotificationDTO}.
 */
@Mapper(componentModel = "spring", uses = { OrgUserMapper.class })
public interface OrgNotificationMapper extends EntityMapper<OrgNotificationDTO, OrgNotification> {
    @Mapping(target = "orgUser", source = "orgUser", qualifiedByName = "id")
    OrgNotificationDTO toDto(OrgNotification s);
}
