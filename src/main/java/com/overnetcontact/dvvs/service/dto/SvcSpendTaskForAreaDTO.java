package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity.
 */
public class SvcSpendTaskForAreaDTO implements Serializable {

    private Long id;

    private SvcAreaDTO svcAreaDTO;

    private Set<SvcSpendTaskDTO> svcSpendTaskDTOs;

    public SvcAreaDTO getSvcAreaDTO() {
        return svcAreaDTO;
    }

    public void setSvcAreaDTO(SvcAreaDTO svcAreaDTO) {
        this.svcAreaDTO = svcAreaDTO;
    }

    public Set<SvcSpendTaskDTO> getSvcSpendTaskDTOs() {
        return svcSpendTaskDTOs;
    }

    public void setSvcSpendTaskDTOs(Set<SvcSpendTaskDTO> svcSpendTaskDTOs) {
        this.svcSpendTaskDTOs = svcSpendTaskDTOs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSpendTaskForAreaDTO)) {
            return false;
        }

        SvcSpendTaskForAreaDTO svcSpendTaskDTO = (SvcSpendTaskForAreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcSpendTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
