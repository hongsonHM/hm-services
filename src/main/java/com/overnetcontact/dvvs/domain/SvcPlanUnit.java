package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcPlanUnit.
 */
@Entity
@Table(name = "svc_plan_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcPlanUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "start_at")
    private LocalDate startAt;

    @Column(name = "end_at")
    private LocalDate endAt;

    @Column(name = "create_at")
    private LocalDate createAt;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "amount_of_work")
    private Integer amountOfWork;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "note")
    private String note;

    @Column(name = "suppervisor_id")
    private Long suppervisorId;

    @OneToMany(mappedBy = "svcPlanUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "svcPlanUnit" }, allowSetters = true)
    private Set<SvcLabor> svcLabors = new HashSet<>();

    @OneToMany(mappedBy = "svcPlanUnit")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Set<SvcPlanTask> svcPlanTasks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "svcPlanUnits" }, allowSetters = true)
    private SvcPlan svcPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcPlanUnit id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getStartAt() {
        return this.startAt;
    }

    public SvcPlanUnit startAt(LocalDate startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    public LocalDate getEndAt() {
        return this.endAt;
    }

    public SvcPlanUnit endAt(LocalDate endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    public LocalDate getCreateAt() {
        return this.createAt;
    }

    public SvcPlanUnit createAt(LocalDate createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public SvcPlanUnit status(Boolean status) {
        this.status = status;
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getAmountOfWork() {
        return this.amountOfWork;
    }

    public SvcPlanUnit amountOfWork(Integer amountOfWork) {
        this.amountOfWork = amountOfWork;
        return this;
    }

    public void setAmountOfWork(Integer amountOfWork) {
        this.amountOfWork = amountOfWork;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public SvcPlanUnit quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public SvcPlanUnit frequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getNote() {
        return this.note;
    }

    public SvcPlanUnit note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getSuppervisorId() {
        return this.suppervisorId;
    }

    public SvcPlanUnit suppervisorId(Long suppervisorId) {
        this.suppervisorId = suppervisorId;
        return this;
    }

    public void setSuppervisorId(Long suppervisorId) {
        this.suppervisorId = suppervisorId;
    }

    public Set<SvcLabor> getSvcLabors() {
        return this.svcLabors;
    }

    public SvcPlanUnit svcLabors(Set<SvcLabor> svcLabors) {
        this.setSvcLabors(svcLabors);
        return this;
    }

    public SvcPlanUnit addSvcLabor(SvcLabor svcLabor) {
        this.svcLabors.add(svcLabor);
        svcLabor.setSvcPlanUnit(this);
        return this;
    }

    public SvcPlanUnit removeSvcLabor(SvcLabor svcLabor) {
        this.svcLabors.remove(svcLabor);
        svcLabor.setSvcPlanUnit(null);
        return this;
    }

    public void setSvcLabors(Set<SvcLabor> svcLabors) {
        if (this.svcLabors != null) {
            this.svcLabors.forEach(i -> i.setSvcPlanUnit(null));
        }
        if (svcLabors != null) {
            svcLabors.forEach(i -> i.setSvcPlanUnit(this));
        }
        this.svcLabors = svcLabors;
    }

    public Set<SvcPlanTask> getSvcPlanTasks() {
        return this.svcPlanTasks;
    }

    public SvcPlanUnit svcPlanTasks(Set<SvcPlanTask> svcPlanTasks) {
        this.setSvcPlanTasks(svcPlanTasks);
        return this;
    }

    public SvcPlanUnit addSvcPlanTask(SvcPlanTask svcPlanTask) {
        this.svcPlanTasks.add(svcPlanTask);
        svcPlanTask.setSvcPlanUnit(this);
        return this;
    }

    public SvcPlanUnit removeSvcPlanTask(SvcPlanTask svcPlanTask) {
        this.svcPlanTasks.remove(svcPlanTask);
        svcPlanTask.setSvcPlanUnit(null);
        return this;
    }

    public void setSvcPlanTasks(Set<SvcPlanTask> svcPlanTasks) {
        if (this.svcPlanTasks != null) {
            this.svcPlanTasks.forEach(i -> i.setSvcPlanUnit(null));
        }
        if (svcPlanTasks != null) {
            svcPlanTasks.forEach(i -> i.setSvcPlanUnit(this));
        }
        this.svcPlanTasks = svcPlanTasks;
    }

    public SvcPlan getSvcPlan() {
        return this.svcPlan;
    }

    public SvcPlanUnit svcPlan(SvcPlan svcPlan) {
        this.setSvcPlan(svcPlan);
        return this;
    }

    public void setSvcPlan(SvcPlan svcPlan) {
        this.svcPlan = svcPlan;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanUnit)) {
            return false;
        }
        return id != null && id.equals(((SvcPlanUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanUnit{" +
            "id=" + getId() +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", status='" + getStatus() + "'" +
            ", amountOfWork=" + getAmountOfWork() +
            ", quantity=" + getQuantity() +
            ", frequency='" + getFrequency() + "'" +
            ", note='" + getNote() + "'" +
            ", suppervisorId=" + getSuppervisorId() +
            "}";
    }
}
