package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcPlan} entity.
 */
public class SvcFullPlanDTO implements Serializable {

    private SvcPlanDTO svcPlanDTO;

    private List<SvcPlanUnitDTO> svcPlanUnitDTOList;

    public SvcPlanDTO getSvcPlanDTO() {
        return svcPlanDTO;
    }

    public void setSvcPlanDTO(SvcPlanDTO svcPlanDTO) {
        this.svcPlanDTO = svcPlanDTO;
    }

    public List<SvcPlanUnitDTO> getSvcPlanUnitDTOList() {
        return svcPlanUnitDTOList;
    }

    public void setSvcPlanUnitDTOList(List<SvcPlanUnitDTO> svcPlanUnitDTOList) {
        this.svcPlanUnitDTOList = svcPlanUnitDTOList;
    }
}
