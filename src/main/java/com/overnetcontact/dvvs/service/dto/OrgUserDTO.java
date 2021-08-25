package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.enumeration.Role;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.OrgUser} entity.
 */
@ApiModel(description = "SERVICE_MANAGER entity of Organization.\n@author KagariV.")
public class OrgUserDTO implements Serializable {

    private Long id;

    private String deviceId;

    @NotNull
    @Size(min = 10, max = 11)
    private String phone;

    @NotNull
    private Role role;

    private AdminUserDTO internalUser;

    private OrgGroupDTO group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AdminUserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(AdminUserDTO internalUser) {
        this.internalUser = internalUser;
    }

    public OrgGroupDTO getGroup() {
        return group;
    }

    public void setGroup(OrgGroupDTO group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUserDTO)) {
            return false;
        }

        OrgUserDTO orgUserDTO = (OrgUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orgUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUserDTO{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", role='" + getRole() + "'" +
            ", internalUser=" + getInternalUser() +
            ", group=" + getGroup() +
            "}";
    }
}
