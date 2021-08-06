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
    private String customerName;

    @NotNull
    private String customerCity;

    @NotNull
    @Size(min = 10, max = 11)
    private String phoneNumber;

    @NotNull
    @Size(max = 300)
    private String type;

    @NotNull
    @Size(max = 300)
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
            ", customerName='" + getCustomerName() + "'" +
            ", customerCity='" + getCustomerCity() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", type='" + getType() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
