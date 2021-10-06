package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcPlanUnit} entity.
 */
public class SvcPlanUnitDTO implements Serializable {

    private Long id;

    private LocalDate startAt;

    private LocalDate endAt;

    private LocalDate createAt;

    private Boolean status;

    private Integer amountOfWork;

    private Integer quantity;

    private String frequency;

    private String note;

    private Long suppervisorId;

    private SvcPlanDTO svcPlan;

    private List<SvcPlanPartDTO> svcPlanPartDTOList;

    public List<SvcPlanPartDTO> getSvcPlanPartDTOList() {
        return svcPlanPartDTOList;
    }

    public void setSvcPlanPartDTOList(List<SvcPlanPartDTO> svcPlanPartDTOList) {
        this.svcPlanPartDTOList = svcPlanPartDTOList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getAmountOfWork() {
        return amountOfWork;
    }

    public void setAmountOfWork(Integer amountOfWork) {
        this.amountOfWork = amountOfWork;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSuppervisorId() {
        return suppervisorId;
    }

    public void setSuppervisorId(Long suppervisorId) {
        this.suppervisorId = suppervisorId;
    }

    public SvcPlanDTO getSvcPlan() {
        return svcPlan;
    }

    public void setSvcPlan(SvcPlanDTO svcPlan) {
        this.svcPlan = svcPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanUnitDTO)) {
            return false;
        }

        SvcPlanUnitDTO svcPlanUnitDTO = (SvcPlanUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcPlanUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanUnitDTO{" +
            "id=" + getId() +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", amountOfWork=" + getAmountOfWork() +
            ", quantity=" + getQuantity() +
            ", frequency='" + getFrequency() + "'" +
            ", note='" + getNote() + "'" +
            ", suppervisorId=" + getSuppervisorId() +
            ", svcPlan=" + getSvcPlan() +
            "}";
    }
}
