package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity.
 */
public class PreviewSuppliesDTO implements Serializable {

    private Long id;

    private String name;

    private String category;

    private String unit;

    private Long totalWaste;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getTotalWaste() {
        return totalWaste;
    }

    public void setTotalWaste(Long totalWaste) {
        this.totalWaste = totalWaste;
    }
}
