package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcTarget} entity.
 */
@ApiModel(description = "Target(which will be work on) entity of SVC.\n@author KagariV.")
public class SvcTargetDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String name;

    private SvcTargetTypeDTO type;

    private SvcTargetDTO svcTarget;

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

    public SvcTargetTypeDTO getType() {
        return type;
    }

    public void setType(SvcTargetTypeDTO type) {
        this.type = type;
    }

    public SvcTargetDTO getSvcTarget() {
        return svcTarget;
    }

    public void setSvcTarget(SvcTargetDTO svcTarget) {
        this.svcTarget = svcTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcTargetDTO)) {
            return false;
        }

        SvcTargetDTO svcTargetDTO = (SvcTargetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcTargetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcTargetDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type=" + getType() +
            ", svcTarget=" + getSvcTarget() +
            "}";
    }
}
