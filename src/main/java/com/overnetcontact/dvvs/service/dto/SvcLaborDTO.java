package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcLabor} entity.
 */
public class SvcLaborDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String phone;

    private String address;

    private String laborCode;

    private SvcPlanUnitDTO svcPlanUnit;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLaborCode() {
        return laborCode;
    }

    public void setLaborCode(String laborCode) {
        this.laborCode = laborCode;
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
        if (!(o instanceof SvcLaborDTO)) {
            return false;
        }

        SvcLaborDTO svcLaborDTO = (SvcLaborDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcLaborDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcLaborDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", laborCode='" + getLaborCode() + "'" +
            ", svcPlanUnit=" + getSvcPlanUnit() +
            "}";
    }
}
