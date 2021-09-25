package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcPlan} entity.
 */
public class SvcPlanDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long serviceManagerId;

    private String serviceManagerName;

    @NotNull
    private Long defaultSuppervisorId;

    private String defaultSuppervisorName;

    private Boolean status;

    private LocalDate startPlan;

    private LocalDate endPlan;

    private LocalDate createDate;

    private Long contractId;

    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServiceManagerId() {
        return serviceManagerId;
    }

    public void setServiceManagerId(Long serviceManagerId) {
        this.serviceManagerId = serviceManagerId;
    }

    public Long getDefaultSuppervisorId() {
        return defaultSuppervisorId;
    }

    public void setDefaultSuppervisorId(Long defaultSuppervisorId) {
        this.defaultSuppervisorId = defaultSuppervisorId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getStartPlan() {
        return startPlan;
    }

    public void setStartPlan(LocalDate startPlan) {
        this.startPlan = startPlan;
    }

    public LocalDate getEndPlan() {
        return endPlan;
    }

    public void setEndPlan(LocalDate endPlan) {
        this.endPlan = endPlan;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getServiceManagerName() {
        return serviceManagerName;
    }

    public void setServiceManagerName(String serviceManagerName) {
        this.serviceManagerName = serviceManagerName;
    }

    public String getDefaultSuppervisorName() {
        return defaultSuppervisorName;
    }

    public void setDefaultSuppervisorName(String defaultSuppervisorName) {
        this.defaultSuppervisorName = defaultSuppervisorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanDTO)) {
            return false;
        }

        SvcPlanDTO svcPlanDTO = (SvcPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", serviceManagerId=" + getServiceManagerId() +
            ", defaultSuppervisorId=" + getDefaultSuppervisorId() +
            ", status='" + getStatus() + "'" +
            ", startPlan='" + getStartPlan() + "'" +
            ", endPlan='" + getEndPlan() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", contractId=" + getContractId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
