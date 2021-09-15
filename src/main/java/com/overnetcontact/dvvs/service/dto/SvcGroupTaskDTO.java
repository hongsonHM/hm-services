package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcGroupTask} entity.
 */
public class SvcGroupTaskDTO implements Serializable {

    private Long id;

    private String name;

    private LocalDate createDate;

    private String createBy;

    private String category;

    private SvcAreaDTO svcArea;

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

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SvcAreaDTO getSvcArea() {
        return svcArea;
    }

    public void setSvcArea(SvcAreaDTO svcArea) {
        this.svcArea = svcArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcGroupTaskDTO)) {
            return false;
        }

        SvcGroupTaskDTO svcGroupTaskDTO = (SvcGroupTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcGroupTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcGroupTaskDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", category='" + getCategory() + "'" +
            ", svcArea=" + getSvcArea() +
            "}";
    }
}
