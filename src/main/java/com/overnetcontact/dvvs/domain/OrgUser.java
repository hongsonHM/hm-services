package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.overnetcontact.dvvs.domain.enumeration.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SERVICE_MANAGER entity of Organization.\n@author KagariV.
 */
@Entity
@Table(name = "org_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrgUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @NotNull
    @Size(min = 10, max = 11)
    @Column(name = "phone", length = 11, nullable = false)
    private String phone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

    @OneToMany(mappedBy = "orgUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orgUser" }, allowSetters = true)
    private Set<OrgNotification> notifications = new HashSet<>();

    @ManyToOne
    private OrgGroup group;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrgUser id(Long id) {
        this.id = id;
        return this;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public OrgUser deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPhone() {
        return this.phone;
    }

    public OrgUser phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Role getRole() {
        return this.role;
    }

    public OrgUser role(Role role) {
        this.role = role;
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public OrgUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public Set<OrgNotification> getNotifications() {
        return this.notifications;
    }

    public OrgUser notifications(Set<OrgNotification> orgNotifications) {
        this.setNotifications(orgNotifications);
        return this;
    }

    public OrgUser addNotifications(OrgNotification orgNotification) {
        this.notifications.add(orgNotification);
        orgNotification.setOrgUser(this);
        return this;
    }

    public OrgUser removeNotifications(OrgNotification orgNotification) {
        this.notifications.remove(orgNotification);
        orgNotification.setOrgUser(null);
        return this;
    }

    public void setNotifications(Set<OrgNotification> orgNotifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setOrgUser(null));
        }
        if (orgNotifications != null) {
            orgNotifications.forEach(i -> i.setOrgUser(this));
        }
        this.notifications = orgNotifications;
    }

    public OrgGroup getGroup() {
        return this.group;
    }

    public OrgUser group(OrgGroup orgGroup) {
        this.setGroup(orgGroup);
        return this;
    }

    public void setGroup(OrgGroup orgGroup) {
        this.group = orgGroup;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgUser)) {
            return false;
        }
        return id != null && id.equals(((OrgUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrgUser{" +
            "id=" + getId() +
            ", deviceId='" + getDeviceId() + "'" +
            ", phone='" + getPhone() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
