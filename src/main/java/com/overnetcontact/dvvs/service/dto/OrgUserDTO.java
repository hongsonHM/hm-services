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

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Size(min = 10, max = 11)
    private String phoneNumber;

    @NotNull
    private Role role;

    private UserDTO internalUser;

    private OrgGroupDTO group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserDTO getInternalUser() {
        return internalUser;
    }

    public void setInternalUser(UserDTO internalUser) {
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
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", role='" + getRole() + "'" +
            ", internalUser=" + getInternalUser() +
            ", group=" + getGroup() +
            "}";
    }
}
