package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSchedulePlan} entity.
 */
@ApiModel(description = "SchedulePlan (big plan) entity of SVC.\n@author KagariV.")
public class SvcSchedulePlanDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean active;

    private OrgUserDTO serviceManager;

    private OrgUserDTO defaultSupervisor;

    private SvcContractDTO contract;

    private SvcScheduleUnitDTO scheduleUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public OrgUserDTO getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(OrgUserDTO serviceManager) {
        this.serviceManager = serviceManager;
    }

    public OrgUserDTO getDefaultSupervisor() {
        return defaultSupervisor;
    }

    public void setDefaultSupervisor(OrgUserDTO defaultSupervisor) {
        this.defaultSupervisor = defaultSupervisor;
    }

    public SvcContractDTO getContract() {
        return contract;
    }

    public void setContract(SvcContractDTO contract) {
        this.contract = contract;
    }

    public SvcScheduleUnitDTO getScheduleUnit() {
        return scheduleUnit;
    }

    public void setScheduleUnit(SvcScheduleUnitDTO scheduleUnit) {
        this.scheduleUnit = scheduleUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSchedulePlanDTO)) {
            return false;
        }

        SvcSchedulePlanDTO svcSchedulePlanDTO = (SvcSchedulePlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcSchedulePlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSchedulePlanDTO{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            ", serviceManager=" + getServiceManager() +
            ", defaultSupervisor=" + getDefaultSupervisor() +
            ", contract=" + getContract() +
            ", scheduleUnit=" + getScheduleUnit() +
            "}";
    }
}
