package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.CoreSupplies} entity.
 */
public class CoreSuppliesDTO implements Serializable {

    private Long id;

    private String name;

    private String unit;

    private String effort;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreSuppliesDTO)) {
            return false;
        }

        CoreSuppliesDTO coreSuppliesDTO = (CoreSuppliesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coreSuppliesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreSuppliesDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", effort='" + getEffort() + "'" +
            "}";
    }
}
