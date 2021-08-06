package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.OrgNotification} entity.
 */
public class OrgNotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String desc;

    private String data;

    @NotNull
    private Boolean isRead;

    @NotNull
    private NotificationStatus status;

    private OrgUserDTO orgUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public OrgUserDTO getOrgUser() {
        return orgUser;
    }

    public void setOrgUser(OrgUserDTO orgUser) {
        this.orgUser = orgUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgNotificationDTO)) {
            return false;
        }

        OrgNotificationDTO orgNotificationDTO = (OrgNotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orgNotificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgNotificationDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", desc='" + getDesc() + "'" +
            ", data='" + getData() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", status='" + getStatus() + "'" +
            ", orgUser=" + getOrgUser() +
            "}";
    }
}
