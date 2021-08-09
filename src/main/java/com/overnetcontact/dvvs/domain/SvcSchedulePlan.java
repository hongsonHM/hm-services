package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SchedulePlan (big plan) entity of SVC.\n@author KagariV.
 */
@Entity
@Table(name = "svc_schedule_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcSchedulePlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @JsonIgnoreProperties(value = { "internalUser", "notifications", "group" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private OrgUser serviceManager;

    @JsonIgnoreProperties(value = { "internalUser", "notifications", "group" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private OrgUser defaultSupervisor;

    @ManyToOne
    @JsonIgnoreProperties(value = { "targets", "approvedBy", "ownerBy", "unit", "saler", "client" }, allowSetters = true)
    private SvcContract contract;

    @ManyToOne
    private SvcScheduleUnit scheduleUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcSchedulePlan id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getActive() {
        return this.active;
    }

    public SvcSchedulePlan active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public OrgUser getServiceManager() {
        return this.serviceManager;
    }

    public SvcSchedulePlan serviceManager(OrgUser orgUser) {
        this.setServiceManager(orgUser);
        return this;
    }

    public void setServiceManager(OrgUser orgUser) {
        this.serviceManager = orgUser;
    }

    public OrgUser getDefaultSupervisor() {
        return this.defaultSupervisor;
    }

    public SvcSchedulePlan defaultSupervisor(OrgUser orgUser) {
        this.setDefaultSupervisor(orgUser);
        return this;
    }

    public void setDefaultSupervisor(OrgUser orgUser) {
        this.defaultSupervisor = orgUser;
    }

    public SvcContract getContract() {
        return this.contract;
    }

    public SvcSchedulePlan contract(SvcContract svcContract) {
        this.setContract(svcContract);
        return this;
    }

    public void setContract(SvcContract svcContract) {
        this.contract = svcContract;
    }

    public SvcScheduleUnit getScheduleUnit() {
        return this.scheduleUnit;
    }

    public SvcSchedulePlan scheduleUnit(SvcScheduleUnit svcScheduleUnit) {
        this.setScheduleUnit(svcScheduleUnit);
        return this;
    }

    public void setScheduleUnit(SvcScheduleUnit svcScheduleUnit) {
        this.scheduleUnit = svcScheduleUnit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSchedulePlan)) {
            return false;
        }
        return id != null && id.equals(((SvcSchedulePlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSchedulePlan{" +
            "id=" + getId() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
