package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.SvcSpendTask;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcPlanPart} entity.
 */
public class SvcPlanPartDTO implements Serializable {

    private Long id;

    @NotNull
    private Long planUnitID;

    //    @NotNull
    //    private Long spendTaskID;

    private SvcSpendTaskDTO svcSpendTask;

    private String name;

    private String unit;

    private String mass;

    private String frequencyDetails;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getFrequencyDetails() {
        return frequencyDetails;
    }

    public void setFrequencyDetails(String frequencyDetails) {
        this.frequencyDetails = frequencyDetails;
    }

    public String getNoteDetails() {
        return noteDetails;
    }

    public void setNoteDetails(String noteDetails) {
        this.noteDetails = noteDetails;
    }

    private String noteDetails;

    private String location;

    private String startAt;

    private String endAt;

    private String frequency;

    private String periodic;

    private String note;

    private String workOnDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanUnitID() {
        return planUnitID;
    }

    public void setPlanUnitID(Long planUnitID) {
        this.planUnitID = planUnitID;
    }

    //    public Long getSpendTaskID() {
    //        return spendTaskID;
    //    }
    //
    //    public void setSpendTaskID(Long spendTaskID) {
    //        this.spendTaskID = spendTaskID;
    //    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPeriodic() {
        return periodic;
    }

    public void setPeriodic(String periodic) {
        this.periodic = periodic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWorkOnDays() {
        return workOnDays;
    }

    public void setWorkOnDays(String workOnDays) {
        this.workOnDays = workOnDays;
    }

    public SvcSpendTaskDTO getSvcSpendTask() {
        return svcSpendTask;
    }

    public void setSvcSpendTask(SvcSpendTaskDTO svcSpendTask) {
        this.svcSpendTask = svcSpendTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanPartDTO)) {
            return false;
        }

        SvcPlanPartDTO svcPlanPartDTO = (SvcPlanPartDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcPlanPartDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanPartDTO{" +
            "id=" + getId() +
            ", planUnitID=" + getPlanUnitID() +
//            ", spendTaskID=" + getSpendTaskID() +
            ", location='" + getLocation() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", periodic='" + getPeriodic() + "'" +
            ", note='" + getNote() + "'" +
            ", workOnDays='" + getWorkOnDays() + "'" +
            "}";
    }
}
