package com.overnetcontact.dvvs.domain;

import com.overnetcontact.dvvs.domain.enumeration.ScheduleStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ScheduleUnit for each unit (break down) entity of SVC.\n@author KagariV.
 */
@Entity
@Table(name = "svc_schedule_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcScheduleUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "start_from", nullable = false)
    private ZonedDateTime startFrom;

    @NotNull
    @Column(name = "end_at", nullable = false)
    private ZonedDateTime endAt;

    @NotNull
    @Column(name = "actual_start_from", nullable = false)
    private ZonedDateTime actualStartFrom;

    @NotNull
    @Column(name = "actual_end_at", nullable = false)
    private ZonedDateTime actualEndAt;

    @NotNull
    @Column(name = "schedule_quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal scheduleQuantity;

    @Column(name = "actual_quantity", precision = 21, scale = 2)
    private BigDecimal actualQuantity;

    @NotNull
    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status;

    @Column(name = "planed_note")
    private String planedNote;

    @Column(name = "laborer_note")
    private String laborerNote;

    @Column(name = "supervisor_note")
    private String supervisorNote;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcScheduleUnit id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getStartFrom() {
        return this.startFrom;
    }

    public SvcScheduleUnit startFrom(ZonedDateTime startFrom) {
        this.startFrom = startFrom;
        return this;
    }

    public void setStartFrom(ZonedDateTime startFrom) {
        this.startFrom = startFrom;
    }

    public ZonedDateTime getEndAt() {
        return this.endAt;
    }

    public SvcScheduleUnit endAt(ZonedDateTime endAt) {
        this.endAt = endAt;
        return this;
    }

    public void setEndAt(ZonedDateTime endAt) {
        this.endAt = endAt;
    }

    public ZonedDateTime getActualStartFrom() {
        return this.actualStartFrom;
    }

    public SvcScheduleUnit actualStartFrom(ZonedDateTime actualStartFrom) {
        this.actualStartFrom = actualStartFrom;
        return this;
    }

    public void setActualStartFrom(ZonedDateTime actualStartFrom) {
        this.actualStartFrom = actualStartFrom;
    }

    public ZonedDateTime getActualEndAt() {
        return this.actualEndAt;
    }

    public SvcScheduleUnit actualEndAt(ZonedDateTime actualEndAt) {
        this.actualEndAt = actualEndAt;
        return this;
    }

    public void setActualEndAt(ZonedDateTime actualEndAt) {
        this.actualEndAt = actualEndAt;
    }

    public BigDecimal getScheduleQuantity() {
        return this.scheduleQuantity;
    }

    public SvcScheduleUnit scheduleQuantity(BigDecimal scheduleQuantity) {
        this.scheduleQuantity = scheduleQuantity;
        return this;
    }

    public void setScheduleQuantity(BigDecimal scheduleQuantity) {
        this.scheduleQuantity = scheduleQuantity;
    }

    public BigDecimal getActualQuantity() {
        return this.actualQuantity;
    }

    public SvcScheduleUnit actualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
        return this;
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
    }

    public LocalDate getAppliedDate() {
        return this.appliedDate;
    }

    public SvcScheduleUnit appliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
        return this;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }

    public ScheduleStatus getStatus() {
        return this.status;
    }

    public SvcScheduleUnit status(ScheduleStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ScheduleStatus status) {
        this.status = status;
    }

    public String getPlanedNote() {
        return this.planedNote;
    }

    public SvcScheduleUnit planedNote(String planedNote) {
        this.planedNote = planedNote;
        return this;
    }

    public void setPlanedNote(String planedNote) {
        this.planedNote = planedNote;
    }

    public String getLaborerNote() {
        return this.laborerNote;
    }

    public SvcScheduleUnit laborerNote(String laborerNote) {
        this.laborerNote = laborerNote;
        return this;
    }

    public void setLaborerNote(String laborerNote) {
        this.laborerNote = laborerNote;
    }

    public String getSupervisorNote() {
        return this.supervisorNote;
    }

    public SvcScheduleUnit supervisorNote(String supervisorNote) {
        this.supervisorNote = supervisorNote;
        return this;
    }

    public void setSupervisorNote(String supervisorNote) {
        this.supervisorNote = supervisorNote;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcScheduleUnit)) {
            return false;
        }
        return id != null && id.equals(((SvcScheduleUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcScheduleUnit{" +
            "id=" + getId() +
            ", startFrom='" + getStartFrom() + "'" +
            ", endAt='" + getEndAt() + "'" +
            ", actualStartFrom='" + getActualStartFrom() + "'" +
            ", actualEndAt='" + getActualEndAt() + "'" +
            ", scheduleQuantity=" + getScheduleQuantity() +
            ", actualQuantity=" + getActualQuantity() +
            ", appliedDate='" + getAppliedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", planedNote='" + getPlanedNote() + "'" +
            ", laborerNote='" + getLaborerNote() + "'" +
            ", supervisorNote='" + getSupervisorNote() + "'" +
            "}";
    }
}
