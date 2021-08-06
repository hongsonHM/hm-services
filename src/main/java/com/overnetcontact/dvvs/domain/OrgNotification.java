package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrgNotification.
 */
@Entity
@Table(name = "org_notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrgNotification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "dvvs_desc", nullable = false)
    private String desc;

    @Column(name = "data")
    private String data;

    @NotNull
    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "notifications", "group" }, allowSetters = true)
    private OrgUser orgUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrgNotification id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public OrgNotification title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public OrgNotification desc(String desc) {
        this.desc = desc;
        return this;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return this.data;
    }

    public OrgNotification data(String data) {
        this.data = data;
        return this;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsRead() {
        return this.isRead;
    }

    public OrgNotification isRead(Boolean isRead) {
        this.isRead = isRead;
        return this;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public OrgNotification status(NotificationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public OrgUser getOrgUser() {
        return this.orgUser;
    }

    public OrgNotification orgUser(OrgUser orgUser) {
        this.setOrgUser(orgUser);
        return this;
    }

    public void setOrgUser(OrgUser orgUser) {
        this.orgUser = orgUser;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgNotification)) {
            return false;
        }
        return id != null && id.equals(((OrgNotification) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgNotification{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", desc='" + getDesc() + "'" +
            ", data='" + getData() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
