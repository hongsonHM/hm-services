package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcUnit} entity.
 */
@ApiModel(description = "Unit entity of SVC.\n@author KagariV.")
public class SvcUnitDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 300)
    private String description;

    private SvcGroupDTO group;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SvcGroupDTO getGroup() {
        return group;
    }

    public void setGroup(SvcGroupDTO group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcUnitDTO)) {
            return false;
        }

        SvcUnitDTO svcUnitDTO = (SvcUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcUnitDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", group=" + getGroup() +
            "}";
    }
}
