package com.overnetcontact.dvvs.domain;

import com.overnetcontact.dvvs.domain.enumeration.Role;
import java.io.Serializable;
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

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Size(min = 10, max = 11)
    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne
    @JoinColumn(unique = true)
    private User internalUser;

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

    public String getEmail() {
        return this.email;
    }

    public OrgUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public OrgUser password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public OrgUser phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", role='" + getRole() + "'" +
            "}";
    }
}
