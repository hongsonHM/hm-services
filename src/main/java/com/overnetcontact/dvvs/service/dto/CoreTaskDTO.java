package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.CoreTask} entity.
 */
public class CoreTaskDTO implements Serializable {

    private Long id;

    private String name;

    private String unit;

    private String category;

    private String note;

    private Set<CoreSuppliesDTO> coreSupplies = new HashSet<>();

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<CoreSuppliesDTO> getCoreSupplies() {
        return coreSupplies;
    }

    public void setCoreSupplies(Set<CoreSuppliesDTO> coreSupplies) {
        this.coreSupplies = coreSupplies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreTaskDTO)) {
            return false;
        }

        CoreTaskDTO coreTaskDTO = (CoreTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, coreTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreTaskDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", category='" + getCategory() + "'" +
            ", note='" + getNote() + "'" +
            ", coreSupplies=" + getCoreSupplies() +
            "}";
    }
}
