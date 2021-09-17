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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcPlanTask} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcPlanTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-plan-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcPlanTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter coreTaskId;

    private StringFilter note;

    private LongFilter svcPlanUnitId;

    public SvcPlanTaskCriteria() {}

    public SvcPlanTaskCriteria(SvcPlanTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.coreTaskId = other.coreTaskId == null ? null : other.coreTaskId.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.svcPlanUnitId = other.svcPlanUnitId == null ? null : other.svcPlanUnitId.copy();
    }

    @Override
    public SvcPlanTaskCriteria copy() {
        return new SvcPlanTaskCriteria(this);
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

    public LongFilter getCoreTaskId() {
        return coreTaskId;
    }

    public LongFilter coreTaskId() {
        if (coreTaskId == null) {
            coreTaskId = new LongFilter();
        }
        return coreTaskId;
    }

    public void setCoreTaskId(LongFilter coreTaskId) {
        this.coreTaskId = coreTaskId;
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

    public LongFilter getSvcPlanUnitId() {
        return svcPlanUnitId;
    }

    public LongFilter svcPlanUnitId() {
        if (svcPlanUnitId == null) {
            svcPlanUnitId = new LongFilter();
        }
        return svcPlanUnitId;
    }

    public void setSvcPlanUnitId(LongFilter svcPlanUnitId) {
        this.svcPlanUnitId = svcPlanUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcPlanTaskCriteria that = (SvcPlanTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(coreTaskId, that.coreTaskId) &&
            Objects.equals(note, that.note) &&
            Objects.equals(svcPlanUnitId, that.svcPlanUnitId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, coreTaskId, note, svcPlanUnitId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanTaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (coreTaskId != null ? "coreTaskId=" + coreTaskId + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (svcPlanUnitId != null ? "svcPlanUnitId=" + svcPlanUnitId + ", " : "") +
            "}";
    }
}
