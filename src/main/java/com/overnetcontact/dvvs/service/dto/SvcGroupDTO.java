package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcGroup} entity.
 */
@ApiModel(description = "Group entity of SVC.\n@author KagariV.")
public class SvcGroupDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 40)
    private String name;

    @NotNull
    @Size(max = 300)
    private String description;

    @NotNull
    @Size(max = 300)
    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcGroupDTO)) {
            return false;
        }

        SvcGroupDTO svcGroupDTO = (SvcGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
