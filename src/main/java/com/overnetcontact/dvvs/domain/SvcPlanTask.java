package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcPlanTask.
 */
@Entity
@Table(name = "svc_plan_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcPlanTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "core_task_id", nullable = false)
    private Long coreTaskId;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "svcLabors", "svcPlanTasks", "svcPlan" }, allowSetters = true)
    private SvcPlanUnit svcPlanUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcPlanTask id(Long id) {
        this.id = id;
        return this;
    }

    public Long getCoreTaskId() {
        return this.coreTaskId;
    }

    public SvcPlanTask coreTaskId(Long coreTaskId) {
        this.coreTaskId = coreTaskId;
        return this;
    }

    public void setCoreTaskId(Long coreTaskId) {
        this.coreTaskId = coreTaskId;
    }

    public String getNote() {
        return this.note;
    }

    public SvcPlanTask note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SvcPlanUnit getSvcPlanUnit() {
        return this.svcPlanUnit;
    }

    public SvcPlanTask svcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.setSvcPlanUnit(svcPlanUnit);
        return this;
    }

    public void setSvcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.svcPlanUnit = svcPlanUnit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcPlanTask)) {
            return false;
        }
        return id != null && id.equals(((SvcPlanTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanTask{" +
            "id=" + getId() +
            ", coreTaskId=" + getCoreTaskId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
