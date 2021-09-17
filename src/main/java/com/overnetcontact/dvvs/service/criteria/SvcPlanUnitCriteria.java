package com.overnetcontact.dvvs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcPlanUnit} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcPlanUnitResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-plan-units?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcPlanUnitCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter startAt;

    private LocalDateFilter endAt;

    private LocalDateFilter createAt;

    private BooleanFilter status;

    private IntegerFilter amountOfWork;

    private IntegerFilter quantity;

    private StringFilter frequency;

    private StringFilter note;

    private LongFilter suppervisorId;

    private LongFilter svcLaborId;

    private LongFilter svcPlanTaskId;

    private LongFilter svcPlanId;

    public SvcPlanUnitCriteria() {}

    public SvcPlanUnitCriteria(SvcPlanUnitCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startAt = other.startAt == null ? null : other.startAt.copy();
        this.endAt = other.endAt == null ? null : other.endAt.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.amountOfWork = other.amountOfWork == null ? null : other.amountOfWork.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.frequency = other.frequency == null ? null : other.frequency.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.suppervisorId = other.suppervisorId == null ? null : other.suppervisorId.copy();
        this.svcLaborId = other.svcLaborId == null ? null : other.svcLaborId.copy();
        this.svcPlanTaskId = other.svcPlanTaskId == null ? null : other.svcPlanTaskId.copy();
        this.svcPlanId = other.svcPlanId == null ? null : other.svcPlanId.copy();
    }

    @Override
    public SvcPlanUnitCriteria copy() {
        return new SvcPlanUnitCriteria(this);
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

    public LocalDateFilter getStartAt() {
        return startAt;
    }

    public LocalDateFilter startAt() {
        if (startAt == null) {
            startAt = new LocalDateFilter();
        }
        return startAt;
    }

    public void setStartAt(LocalDateFilter startAt) {
        this.startAt = startAt;
    }

    public LocalDateFilter getEndAt() {
        return endAt;
    }

    public LocalDateFilter endAt() {
        if (endAt == null) {
            endAt = new LocalDateFilter();
        }
        return endAt;
    }

    public void setEndAt(LocalDateFilter endAt) {
        this.endAt = endAt;
    }

    public LocalDateFilter getCreateAt() {
        return createAt;
    }

    public LocalDateFilter createAt() {
        if (createAt == null) {
            createAt = new LocalDateFilter();
        }
        return createAt;
    }

    public void setCreateAt(LocalDateFilter createAt) {
        this.createAt = createAt;
    }

    public BooleanFilter getStatus() {
        return status;
    }

    public BooleanFilter status() {
        if (status == null) {
            status = new BooleanFilter();
        }
        return status;
    }

    public void setStatus(BooleanFilter status) {
        this.status = status;
    }

    public IntegerFilter getAmountOfWork() {
        return amountOfWork;
    }

    public IntegerFilter amountOfWork() {
        if (amountOfWork == null) {
            amountOfWork = new IntegerFilter();
        }
        return amountOfWork;
    }

    public void setAmountOfWork(IntegerFilter amountOfWork) {
        this.amountOfWork = amountOfWork;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            quantity = new IntegerFilter();
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
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

    public LongFilter getSuppervisorId() {
        return suppervisorId;
    }

    public LongFilter suppervisorId() {
        if (suppervisorId == null) {
            suppervisorId = new LongFilter();
        }
        return suppervisorId;
    }

    public void setSuppervisorId(LongFilter suppervisorId) {
        this.suppervisorId = suppervisorId;
    }

    public LongFilter getSvcLaborId() {
        return svcLaborId;
    }

    public LongFilter svcLaborId() {
        if (svcLaborId == null) {
            svcLaborId = new LongFilter();
        }
        return svcLaborId;
    }

    public void setSvcLaborId(LongFilter svcLaborId) {
        this.svcLaborId = svcLaborId;
    }

    public LongFilter getSvcPlanTaskId() {
        return svcPlanTaskId;
    }

    public LongFilter svcPlanTaskId() {
        if (svcPlanTaskId == null) {
            svcPlanTaskId = new LongFilter();
        }
        return svcPlanTaskId;
    }

    public void setSvcPlanTaskId(LongFilter svcPlanTaskId) {
        this.svcPlanTaskId = svcPlanTaskId;
    }

    public LongFilter getSvcPlanId() {
        return svcPlanId;
    }

    public LongFilter svcPlanId() {
        if (svcPlanId == null) {
            svcPlanId = new LongFilter();
        }
        return svcPlanId;
    }

    public void setSvcPlanId(LongFilter svcPlanId) {
        this.svcPlanId = svcPlanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcPlanUnitCriteria that = (SvcPlanUnitCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(endAt, that.endAt) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(status, that.status) &&
            Objects.equals(amountOfWork, that.amountOfWork) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(note, that.note) &&
            Objects.equals(suppervisorId, that.suppervisorId) &&
            Objects.equals(svcLaborId, that.svcLaborId) &&
            Objects.equals(svcPlanTaskId, that.svcPlanTaskId) &&
            Objects.equals(svcPlanId, that.svcPlanId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startAt,
            endAt,
            createAt,
            status,
            amountOfWork,
            quantity,
            frequency,
            note,
            suppervisorId,
            svcLaborId,
            svcPlanTaskId,
            svcPlanId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanUnitCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startAt != null ? "startAt=" + startAt + ", " : "") +
            (endAt != null ? "endAt=" + endAt + ", " : "") +
            (createAt != null ? "createAt=" + createAt + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (amountOfWork != null ? "amountOfWork=" + amountOfWork + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (frequency != null ? "frequency=" + frequency + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (suppervisorId != null ? "suppervisorId=" + suppervisorId + ", " : "") +
            (svcLaborId != null ? "svcLaborId=" + svcLaborId + ", " : "") +
            (svcPlanTaskId != null ? "svcPlanTaskId=" + svcPlanTaskId + ", " : "") +
            (svcPlanId != null ? "svcPlanId=" + svcPlanId + ", " : "") +
            "}";
    }
}
