package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity.
 */
public class SvcFullContractsDTO implements Serializable {

    private SvcContractDTO svcContractDTO;

    private Set<SvcSpendTaskForAreaDTO> svcSpendTaskForAreaDTOs;

    public SvcContractDTO getSvcContractDTO() {
        return svcContractDTO;
    }

    public void setSvcContractDTO(SvcContractDTO svcContractDTO) {
        this.svcContractDTO = svcContractDTO;
    }

    public Set<SvcSpendTaskForAreaDTO> getSvcSpendTaskForAreaDTOs() {
        return svcSpendTaskForAreaDTOs;
    }

    public void setSvcSpendTaskForAreaDTOs(Set<SvcSpendTaskForAreaDTO> svcSpendTaskForAreaDTOs) {
        this.svcSpendTaskForAreaDTOs = svcSpendTaskForAreaDTOs;
    }
}
