package com.overnetcontact.dvvs.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcPlanPart.
 */
@Entity
@Table(name = "svc_plan_part")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcPlanPart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "plan_unit_id", nullable = false)
    private Long planUnitID;

    @NotNull
    @Column(name = "spend_task_id", nullable = false)
    private Long spendTaskID;

    @Column(name = "location")
    private String location;

    @Column(name = "start_at")
    private String startAt;

    @Column(name = "end_at")
    private String endAt;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "periodic")
    private String periodic;

    @Column(name = "note")
    private String note;

    @Column(name = "work_on_days")
    private String workOnDays;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcPlanPart id(Long id) {
        this.id = id;
        return this;
    }

    public Long getPlanUnitID() {
        return this.planUnitID;
    }

    public SvcPlanPart planUnitID(Long planUnitID) {
        this.planUnitID = planUnitID;
        return this;
    }

    public void setPlanUnitID(Long planUnitID) {
        this.planUnitID = planUnitID;
    }

    public Long getSpendTaskID() {
        return this.spendTaskID;
    }

    public SvcPlanPart spendTaskID(Long spendTaskID) {
        this.spendTaskID = spendTaskID;
        return this;
    }

    public void setSpendTaskID(Long spendTaskID) {
        this.spendTaskID = spendTaskID;
    }

    public String getLocation() {
        return this.location;
    }

    public SvcPlanPart location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartAt() {
        return this.startAt;
    }

    public SvcPlanPart startAt(String startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getEndAt() {
        return this.endAt;
    }

    public SvcPlanPart endAt(String endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public SvcPlanPart frequency(String frequency) {
        this.frequency = frequency;
        return this;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPeriodic() {
        return this.periodic;
    }

    public SvcPlanPart periodic(String periodic) {
        this.periodic = periodic;
        return this;
    }

    public void setPeriodic(String periodic) {
        this.periodic = periodic;
    }

    public String getNote() {
        return this.note;
    }

    public SvcPlanPart note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWorkOnDays() {
        return this.workOnDays;
    }

    public SvcPlanPart workOnDays(String workOnDays) {
        this.workOnDays = workOnDays;
        return this;
    }

    public void setWorkOnDays(String workOnDays) {
        this.workOnDays = workOnDays;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanPart)) {
            return false;
        }
        return id != null && id.equals(((SvcPlanPart) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanPart{" +
            "id=" + getId() +
            ", planUnitID=" + getPlanUnitID() +
            ", spendTaskID=" + getSpendTaskID() +
            ", location='" + getLocation() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", frequency='" + getFrequency() + "'" +
            ", periodic='" + getPeriodic() + "'" +
            ", note='" + getNote() + "'" +
            ", workOnDays='" + getWorkOnDays() + "'" +
            "}";
    }
}
