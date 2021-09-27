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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcSpendTask} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcSpendTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-spend-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcSpendTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter coreTaskId;

    private StringFilter mass;

    private StringFilter frequency;

    private StringFilter note;

    private LongFilter svcGroupTaskId;

    public SvcSpendTaskCriteria() {}

    public SvcSpendTaskCriteria(SvcSpendTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.coreTaskId = other.coreTaskId == null ? null : other.coreTaskId.copy();
        this.mass = other.mass == null ? null : other.mass.copy();
        this.frequency = other.frequency == null ? null : other.frequency.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.svcGroupTaskId = other.svcGroupTaskId == null ? null : other.svcGroupTaskId.copy();
    }

    @Override
    public SvcSpendTaskCriteria copy() {
        return new SvcSpendTaskCriteria(this);
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

    public IntegerFilter getCoreTaskId() {
        return coreTaskId;
    }

    public IntegerFilter coreTaskId() {
        if (coreTaskId == null) {
            coreTaskId = new IntegerFilter();
        }
        return coreTaskId;
    }

    public void setCoreTaskId(IntegerFilter coreTaskId) {
        this.coreTaskId = coreTaskId;
    }

    public StringFilter getMass() {
        return mass;
    }

    public StringFilter mass() {
        if (mass == null) {
            mass = new StringFilter();
        }
        return mass;
    }

    public void setMass(StringFilter mass) {
        this.mass = mass;
    }

    public StringFilter getFrequency() {
        return mass;
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

    public LongFilter getSvcGroupTaskId() {
        return svcGroupTaskId;
    }

    public LongFilter svcGroupTaskId() {
        if (svcGroupTaskId == null) {
            svcGroupTaskId = new LongFilter();
        }
        return svcGroupTaskId;
    }

    public void setSvcGroupTaskId(LongFilter svcGroupTaskId) {
        this.svcGroupTaskId = svcGroupTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcSpendTaskCriteria that = (SvcSpendTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(coreTaskId, that.coreTaskId) &&
            Objects.equals(mass, that.mass) &&
            Objects.equals(note, that.note) &&
            Objects.equals(svcGroupTaskId, that.svcGroupTaskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coreTaskId, mass, note, svcGroupTaskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSpendTaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (coreTaskId != null ? "coreTaskId=" + coreTaskId + ", " : "") +
            (mass != null ? "mass=" + mass + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (svcGroupTaskId != null ? "svcGroupTaskId=" + svcGroupTaskId + ", " : "") +
            "}";
    }
}
