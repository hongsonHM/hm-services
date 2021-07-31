package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.enumeration.ScheduleStatus;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcScheduleUnit} entity.
 */
@ApiModel(description = "ScheduleUnit for each unit (break down) entity of SVC.\n@author KagariV.")
public class SvcScheduleUnitDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime startFrom;

    @NotNull
    private ZonedDateTime endAt;

    @NotNull
    private ZonedDateTime actualStartFrom;

    @NotNull
    private ZonedDateTime actualEndAt;

    @NotNull
    private BigDecimal scheduleQuantity;

    private BigDecimal actualQuantity;

    @NotNull
    private LocalDate appliedDate;

    @NotNull
    private ScheduleStatus status;

    private String planedNote;

    private String laborerNote;

    private String supervisorNote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(ZonedDateTime startFrom) {
        this.startFrom = startFrom;
    }

    public ZonedDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(ZonedDateTime endAt) {
        this.endAt = endAt;
    }

    public ZonedDateTime getActualStartFrom() {
        return actualStartFrom;
    }

    public void setActualStartFrom(ZonedDateTime actualStartFrom) {
        this.actualStartFrom = actualStartFrom;
    }

    public ZonedDateTime getActualEndAt() {
        return actualEndAt;
    }

    public void setActualEndAt(ZonedDateTime actualEndAt) {
        this.actualEndAt = actualEndAt;
    }

    public BigDecimal getScheduleQuantity() {
        return scheduleQuantity;
    }

    public void setScheduleQuantity(BigDecimal scheduleQuantity) {
        this.scheduleQuantity = scheduleQuantity;
    }

    public BigDecimal getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ScheduleStatus getStatus() {
        return status;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getPlanedNote() {
        return planedNote;
    }

    public void setPlanedNote(String planedNote) {
        this.planedNote = planedNote;
    }

    public String getLaborerNote() {
        return laborerNote;
    }

    public void setLaborerNote(String laborerNote) {
        this.laborerNote = laborerNote;
    }

    public String getSupervisorNote() {
        return supervisorNote;
    }

    public void setSupervisorNote(String supervisorNote) {
        this.supervisorNote = supervisorNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcScheduleUnitDTO)) {
            return false;
        }

        SvcScheduleUnitDTO svcScheduleUnitDTO = (SvcScheduleUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcScheduleUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcScheduleUnitDTO{" +
            "id=" + getId() +
            ", startFrom='" + getStartFrom() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", actualStartFrom='" + getActualStartFrom() + "'" +
            ", actualEndAt='" + getActualEndAt() + "'" +
            ", scheduleQuantity=" + getScheduleQuantity() +
            ", actualQuantity=" + getActualQuantity() +
            ", appliedDate='" + getAppliedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", planedNote='" + getPlanedNote() + "'" +
            ", laborerNote='" + getLaborerNote() + "'" +
            ", supervisorNote='" + getSupervisorNote() + "'" +
            "}";
    }
}
