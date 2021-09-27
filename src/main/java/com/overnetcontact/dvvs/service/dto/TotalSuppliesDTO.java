package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity.
 */
public class TotalSuppliesDTO implements Serializable {

    private Set<SvcSpendTaskDTO> svcSpendTaskDTOs;

    public Set<SvcSpendTaskDTO> getSvcSpendTaskDTOs() {
        return svcSpendTaskDTOs;
    }

    public void setSvcSpendTaskDTOs(Set<SvcSpendTaskDTO> svcSpendTaskDTOs) {
        this.svcSpendTaskDTOs = svcSpendTaskDTOs;
    }
}
