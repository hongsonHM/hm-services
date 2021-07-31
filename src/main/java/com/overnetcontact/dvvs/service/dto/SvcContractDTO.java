package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcContract} entity.
 */
@ApiModel(description = "SvcContract entity of SVC.\n@author KagariV.")
public class SvcContractDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    private String name;

    @NotNull
    private Instant effectiveDate;

    @NotNull
    private Instant expirationDate;

    private SvcUnitDTO unit;

    private OrgUserDTO saler;

    private SvcClientDTO client;

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

    public Instant getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Instant effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SvcUnitDTO getUnit() {
        return unit;
    }

    public void setUnit(SvcUnitDTO unit) {
        this.unit = unit;
    }

    public OrgUserDTO getSaler() {
        return saler;
    }

    public void setSaler(OrgUserDTO saler) {
        this.saler = saler;
    }

    public SvcClientDTO getClient() {
        return client;
    }

    public void setClient(SvcClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcContractDTO)) {
            return false;
        }

        SvcContractDTO svcContractDTO = (SvcContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcContractDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", unit=" + getUnit() +
            ", saler=" + getSaler() +
            ", client=" + getClient() +
            "}";
    }
}
