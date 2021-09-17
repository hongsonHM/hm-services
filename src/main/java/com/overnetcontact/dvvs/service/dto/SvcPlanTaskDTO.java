package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcPlanTask} entity.
 */
public class SvcPlanTaskDTO implements Serializable {

    private Long id;

    @NotNull
    private Long coreTaskId;

    private String note;

    private SvcPlanUnitDTO svcPlanUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoreTaskId() {
        return coreTaskId;
    }

    public void setCoreTaskId(Long coreTaskId) {
        this.coreTaskId = coreTaskId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SvcPlanUnitDTO getSvcPlanUnit() {
        return svcPlanUnit;
    }

    public void setSvcPlanUnit(SvcPlanUnitDTO svcPlanUnit) {
        this.svcPlanUnit = svcPlanUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanTaskDTO)) {
            return false;
        }

        SvcPlanTaskDTO svcPlanTaskDTO = (SvcPlanTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcPlanTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanTaskDTO{" +
            "id=" + getId() +
            ", coreTaskId=" + getCoreTaskId() +
            ", note='" + getNote() + "'" +
            ", svcPlanUnit=" + getSvcPlanUnit() +
            "}";
    }
}
