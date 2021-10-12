package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.CoreTask;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity.
 */
public class SvcSpendTaskDTO implements Serializable {

    private Long id;

    //    private Long coreTaskId;

    private CoreTaskDTO coreTask;

    private String mass;

    private String frequency;

    private String note;

    private SvcGroupTaskDTO svcGroupTask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //    public Long getCoreTaskId() {
    //        return coreTaskId;
    //    }
    //
    //    public void setCoreTaskId(Long coreTaskId) {
    //        this.coreTaskId = coreTaskId;
    //    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SvcGroupTaskDTO getSvcGroupTask() {
        return svcGroupTask;
    }

    public void setSvcGroupTask(SvcGroupTaskDTO svcGroupTask) {
        this.svcGroupTask = svcGroupTask;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public CoreTaskDTO getCoreTask() {
        return coreTask;
    }

    public void setCoreTask(CoreTaskDTO coreTask) {
        this.coreTask = coreTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSpendTaskDTO)) {
            return false;
        }

        SvcSpendTaskDTO svcSpendTaskDTO = (SvcSpendTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcSpendTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSpendTaskDTO{" +
            "id=" + getId() +
//            ", coreTaskId=" + getCoreTaskId() +
            ", mass='" + getMass() + "'" +
            ", note='" + getNote() + "'" +
            ", svcGroupTask=" + getSvcGroupTask() +
            "}";
    }
}
