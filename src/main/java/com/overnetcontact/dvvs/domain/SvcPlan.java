package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcPlan.
 */
@Entity
@Table(name = "svc_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "service_manager_id", nullable = false)
    private Long serviceManagerId;

    @NotNull
    @Column(name = "default_suppervisor_id", nullable = false)
    private Long defaultSuppervisorId;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "start_plan")
    private LocalDate startPlan;

    @Column(name = "end_plan")
    private LocalDate endPlan;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "contract_id")
    private Long contractId;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "svcPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Set<SvcPlanUnit> svcPlanUnits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcPlan id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SvcPlan name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getServiceManagerId() {
        return this.serviceManagerId;
    }

    public SvcPlan serviceManagerId(Long serviceManagerId) {
        this.serviceManagerId = serviceManagerId;
        return this;
    }

    public void setServiceManagerId(Long serviceManagerId) {
        this.serviceManagerId = serviceManagerId;
    }

    public Long getDefaultSuppervisorId() {
        return this.defaultSuppervisorId;
    }

    public SvcPlan defaultSuppervisorId(Long defaultSuppervisorId) {
        this.defaultSuppervisorId = defaultSuppervisorId;
        return this;
    }

    public void setDefaultSuppervisorId(Long defaultSuppervisorId) {
        this.defaultSuppervisorId = defaultSuppervisorId;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public SvcPlan status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LocalDate getStartPlan() {
        return this.startPlan;
    }

    public SvcPlan startPlan(LocalDate startPlan) {
        this.startPlan = startPlan;
        return this;
    }

    public void setStartPlan(LocalDate startPlan) {
        this.startPlan = startPlan;
    }

    public LocalDate getEndPlan() {
        return this.endPlan;
    }

    public SvcPlan endPlan(LocalDate endPlan) {
        this.endPlan = endPlan;
        return this;
    }

    public void setEndPlan(LocalDate endPlan) {
        this.endPlan = endPlan;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SvcPlan createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public Long getContractId() {
        return this.contractId;
    }

    public SvcPlan contractId(Long contractId) {
        this.contractId = contractId;
        return this;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getNote() {
        return this.note;
    }

    public SvcPlan note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<SvcPlanUnit> getSvcPlanUnits() {
        return this.svcPlanUnits;
    }

    public SvcPlan svcPlanUnits(Set<SvcPlanUnit> svcPlanUnits) {
        this.setSvcPlanUnits(svcPlanUnits);
        return this;
    }

    public SvcPlan addSvcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.svcPlanUnits.add(svcPlanUnit);
        svcPlanUnit.setSvcPlan(this);
        return this;
    }

    public SvcPlan removeSvcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.svcPlanUnits.remove(svcPlanUnit);
        svcPlanUnit.setSvcPlan(null);
        return this;
    }

    public void setSvcPlanUnits(Set<SvcPlanUnit> svcPlanUnits) {
        if (this.svcPlanUnits != null) {
            this.svcPlanUnits.forEach(i -> i.setSvcPlan(null));
        }
        if (svcPlanUnits != null) {
            svcPlanUnits.forEach(i -> i.setSvcPlan(this));
        }
        this.svcPlanUnits = svcPlanUnits;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlan)) {
            return false;
        }
        return id != null && id.equals(((SvcPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlan{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", serviceManagerId=" + getServiceManagerId() +
            ", defaultSuppervisorId=" + getDefaultSuppervisorId() +
            ", status='" + getStatus() + "'" +
            ", startPlan='" + getStartPlan() + "'" +
            ", endPlan='" + getEndPlan() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", contractId=" + getContractId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
