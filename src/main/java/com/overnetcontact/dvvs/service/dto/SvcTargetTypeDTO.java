package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcTargetType} entity.
 */
@ApiModel(description = "TargetType(what type of target) entity of SVC.\n@author KagariV.")
public class SvcTargetTypeDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcTargetTypeDTO)) {
            return false;
        }

        SvcTargetTypeDTO svcTargetTypeDTO = (SvcTargetTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcTargetTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcTargetTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
