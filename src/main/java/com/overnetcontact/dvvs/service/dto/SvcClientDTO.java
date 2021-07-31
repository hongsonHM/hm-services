package com.overnetcontact.dvvs.service.dto;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcClient} entity.
 */
@ApiModel(description = "Client entity of SVC.\n@author KagariV.")
public class SvcClientDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 6, max = 40)
    private String name;

    @NotNull
    @Size(min = 10, max = 11)
    private String phoneNumber;

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        if (!(o instanceof SvcClientDTO)) {
            return false;
        }

        SvcClientDTO svcClientDTO = (SvcClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcClientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcClientDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
