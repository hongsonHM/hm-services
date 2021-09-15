package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcArea} entity.
 */
public class SvcAreaDTO implements Serializable {

    private Long id;

    private String name;

    private String location;

    private String type;

    private Integer contractsId;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getContractsId() {
        return contractsId;
    }

    public void setContractsId(Integer contractsId) {
        this.contractsId = contractsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcAreaDTO)) {
            return false;
        }

        SvcAreaDTO svcAreaDTO = (SvcAreaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcAreaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcAreaDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", type='" + getType() + "'" +
            ", contractsId=" + getContractsId() +
            "}";
    }
}
