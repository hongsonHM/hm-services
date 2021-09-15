package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcSpendTask.
 */
@Entity
@Table(name = "svc_spend_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcSpendTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "core_task_id")
    private Integer coreTaskId;

    @Column(name = "mass")
    private String mass;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "svcArea", "svcSpendTasks" }, allowSetters = true)
    private SvcGroupTask svcGroupTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcSpendTask id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCoreTaskId() {
        return this.coreTaskId;
    }

    public SvcSpendTask coreTaskId(Integer coreTaskId) {
        this.coreTaskId = coreTaskId;
        return this;
    }

    public void setCoreTaskId(Integer coreTaskId) {
        this.coreTaskId = coreTaskId;
    }

    public String getMass() {
        return this.mass;
    }

    public SvcSpendTask mass(String mass) {
        this.mass = mass;
        return this;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getNote() {
        return this.note;
    }

    public SvcSpendTask note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SvcGroupTask getSvcGroupTask() {
        return this.svcGroupTask;
    }

    public SvcSpendTask svcGroupTask(SvcGroupTask svcGroupTask) {
        this.setSvcGroupTask(svcGroupTask);
        return this;
    }

    public void setSvcGroupTask(SvcGroupTask svcGroupTask) {
        this.svcGroupTask = svcGroupTask;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSpendTask)) {
            return false;
        }
        return id != null && id.equals(((SvcSpendTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSpendTask{" +
            "id=" + getId() +
            ", coreTaskId=" + getCoreTaskId() +
            ", mass='" + getMass() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
