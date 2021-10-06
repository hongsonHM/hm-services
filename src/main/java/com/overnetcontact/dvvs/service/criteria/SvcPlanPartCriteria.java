package com.overnetcontact.dvvs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcPlanPart} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcPlanPartResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-plan-parts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcPlanPartCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter planUnitID;

    private LongFilter spendTaskID;

    private StringFilter location;

    private StringFilter startAt;

    private StringFilter endAt;

    private StringFilter frequency;

    private StringFilter periodic;

    private StringFilter note;

    private StringFilter workOnDays;

    public SvcPlanPartCriteria() {}

    public SvcPlanPartCriteria(SvcPlanPartCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.planUnitID = other.planUnitID == null ? null : other.planUnitID.copy();
        this.spendTaskID = other.spendTaskID == null ? null : other.spendTaskID.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.startAt = other.startAt == null ? null : other.startAt.copy();
        this.endAt = other.endAt == null ? null : other.endAt.copy();
        this.frequency = other.frequency == null ? null : other.frequency.copy();
        this.periodic = other.periodic == null ? null : other.periodic.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.workOnDays = other.workOnDays == null ? null : other.workOnDays.copy();
    }

    @Override
    public SvcPlanPartCriteria copy() {
        return new SvcPlanPartCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getPlanUnitID() {
        return planUnitID;
    }

    public LongFilter planUnitID() {
        if (planUnitID == null) {
            planUnitID = new LongFilter();
        }
        return planUnitID;
    }

    public void setPlanUnitID(LongFilter planUnitID) {
        this.planUnitID = planUnitID;
    }

    public LongFilter getSpendTaskID() {
        return spendTaskID;
    }

    public LongFilter spendTaskID() {
        if (spendTaskID == null) {
            spendTaskID = new LongFilter();
        }
        return spendTaskID;
    }

    public void setSpendTaskID(LongFilter spendTaskID) {
        this.spendTaskID = spendTaskID;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getStartAt() {
        return startAt;
    }

    public StringFilter startAt() {
        if (startAt == null) {
            startAt = new StringFilter();
        }
        return startAt;
    }

    public void setStartAt(StringFilter startAt) {
        this.startAt = startAt;
    }

    public StringFilter getEndAt() {
        return endAt;
    }

    public StringFilter endAt() {
        if (endAt == null) {
            endAt = new StringFilter();
        }
        return endAt;
    }

    public void setEndAt(StringFilter endAt) {
        this.endAt = endAt;
    }

    public StringFilter getFrequency() {
        return frequency;
    }

    public StringFilter frequency() {
        if (frequency == null) {
            frequency = new StringFilter();
        }
        return frequency;
    }

    public void setFrequency(StringFilter frequency) {
        this.frequency = frequency;
    }

    public StringFilter getPeriodic() {
        return periodic;
    }

    public StringFilter periodic() {
        if (periodic == null) {
            periodic = new StringFilter();
        }
        return periodic;
    }

    public void setPeriodic(StringFilter periodic) {
        this.periodic = periodic;
    }

    public StringFilter getNote() {
        return note;
    }

    public StringFilter note() {
        if (note == null) {
            note = new StringFilter();
        }
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public StringFilter getWorkOnDays() {
        return workOnDays;
    }

    public StringFilter workOnDays() {
        if (workOnDays == null) {
            workOnDays = new StringFilter();
        }
        return workOnDays;
    }

    public void setWorkOnDays(StringFilter workOnDays) {
        this.workOnDays = workOnDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcPlanPartCriteria that = (SvcPlanPartCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(planUnitID, that.planUnitID) &&
            Objects.equals(spendTaskID, that.spendTaskID) &&
            Objects.equals(location, that.location) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(periodic, that.periodic) &&
            Objects.equals(note, that.note) &&
            Objects.equals(workOnDays, that.workOnDays)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, planUnitID, spendTaskID, location, startAt, endAt, frequency, periodic, note, workOnDays);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanPartCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (planUnitID != null ? "planUnitID=" + planUnitID + ", " : "") +
            (spendTaskID != null ? "spendTaskID=" + spendTaskID + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (startAt != null ? "startAt=" + startAt + ", " : "") +
            (endAt != null ? "endAt=" + endAt + ", " : "") +
            (frequency != null ? "frequency=" + frequency + ", " : "") +
            (periodic != null ? "periodic=" + periodic + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (workOnDays != null ? "workOnDays=" + workOnDays + ", " : "") +
            "}";
    }
}
