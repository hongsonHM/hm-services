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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcScheduleUnitRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-schedule-unit-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcScheduleUnitRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter svcScheduleUnitId;

    private IntegerFilter isPublished;

    private StringFilter status;

    private StringFilter approved;

    private IntegerFilter approvedId;

    private StringFilter comment;

    private LocalDateFilter createDate;

    private LocalDateFilter lastModifiedDate;

    private StringFilter createBy;

    private IntegerFilter createById;

    private StringFilter lastModifiedBy;

    private IntegerFilter lastModifiedById;

    private LongFilter svcSchedulePlanRecordId;

    public SvcScheduleUnitRecordCriteria() {}

    public SvcScheduleUnitRecordCriteria(SvcScheduleUnitRecordCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.svcScheduleUnitId = other.svcScheduleUnitId == null ? null : other.svcScheduleUnitId.copy();
        this.isPublished = other.isPublished == null ? null : other.isPublished.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.approved = other.approved == null ? null : other.approved.copy();
        this.approvedId = other.approvedId == null ? null : other.approvedId.copy();
        this.comment = other.comment == null ? null : other.comment.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.lastModifiedDate = other.lastModifiedDate == null ? null : other.lastModifiedDate.copy();
        this.createBy = other.createBy == null ? null : other.createBy.copy();
        this.createById = other.createById == null ? null : other.createById.copy();
        this.lastModifiedBy = other.lastModifiedBy == null ? null : other.lastModifiedBy.copy();
        this.lastModifiedById = other.lastModifiedById == null ? null : other.lastModifiedById.copy();
        this.svcSchedulePlanRecordId = other.svcSchedulePlanRecordId == null ? null : other.svcSchedulePlanRecordId.copy();
    }

    @Override
    public SvcScheduleUnitRecordCriteria copy() {
        return new SvcScheduleUnitRecordCriteria(this);
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

    public IntegerFilter getSvcScheduleUnitId() {
        return svcScheduleUnitId;
    }

    public IntegerFilter svcScheduleUnitId() {
        if (svcScheduleUnitId == null) {
            svcScheduleUnitId = new IntegerFilter();
        }
        return svcScheduleUnitId;
    }

    public void setSvcScheduleUnitId(IntegerFilter svcScheduleUnitId) {
        this.svcScheduleUnitId = svcScheduleUnitId;
    }

    public IntegerFilter getIsPublished() {
        return isPublished;
    }

    public IntegerFilter isPublished() {
        if (isPublished == null) {
            isPublished = new IntegerFilter();
        }
        return isPublished;
    }

    public void setIsPublished(IntegerFilter isPublished) {
        this.isPublished = isPublished;
    }

    public StringFilter getStatus() {
        return status;
    }

    public StringFilter status() {
        if (status == null) {
            status = new StringFilter();
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getApproved() {
        return approved;
    }

    public StringFilter approved() {
        if (approved == null) {
            approved = new StringFilter();
        }
        return approved;
    }

    public void setApproved(StringFilter approved) {
        this.approved = approved;
    }

    public IntegerFilter getApprovedId() {
        return approvedId;
    }

    public IntegerFilter approvedId() {
        if (approvedId == null) {
            approvedId = new IntegerFilter();
        }
        return approvedId;
    }

    public void setApprovedId(IntegerFilter approvedId) {
        this.approvedId = approvedId;
    }

    public StringFilter getComment() {
        return comment;
    }

    public StringFilter comment() {
        if (comment == null) {
            comment = new StringFilter();
        }
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public LocalDateFilter getCreateDate() {
        return createDate;
    }

    public LocalDateFilter createDate() {
        if (createDate == null) {
            createDate = new LocalDateFilter();
        }
        return createDate;
    }

    public void setCreateDate(LocalDateFilter createDate) {
        this.createDate = createDate;
    }

    public LocalDateFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public LocalDateFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            lastModifiedDate = new LocalDateFilter();
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public StringFilter getCreateBy() {
        return createBy;
    }

    public StringFilter createBy() {
        if (createBy == null) {
            createBy = new StringFilter();
        }
        return createBy;
    }

    public void setCreateBy(StringFilter createBy) {
        this.createBy = createBy;
    }

    public IntegerFilter getCreateById() {
        return createById;
    }

    public IntegerFilter createById() {
        if (createById == null) {
            createById = new IntegerFilter();
        }
        return createById;
    }

    public void setCreateById(IntegerFilter createById) {
        this.createById = createById;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            lastModifiedBy = new StringFilter();
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public IntegerFilter getLastModifiedById() {
        return lastModifiedById;
    }

    public IntegerFilter lastModifiedById() {
        if (lastModifiedById == null) {
            lastModifiedById = new IntegerFilter();
        }
        return lastModifiedById;
    }

    public void setLastModifiedById(IntegerFilter lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public LongFilter getSvcSchedulePlanRecordId() {
        return svcSchedulePlanRecordId;
    }

    public LongFilter svcSchedulePlanRecordId() {
        if (svcSchedulePlanRecordId == null) {
            svcSchedulePlanRecordId = new LongFilter();
        }
        return svcSchedulePlanRecordId;
    }

    public void setSvcSchedulePlanRecordId(LongFilter svcSchedulePlanRecordId) {
        this.svcSchedulePlanRecordId = svcSchedulePlanRecordId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcScheduleUnitRecordCriteria that = (SvcScheduleUnitRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(svcScheduleUnitId, that.svcScheduleUnitId) &&
            Objects.equals(isPublished, that.isPublished) &&
            Objects.equals(status, that.status) &&
            Objects.equals(approved, that.approved) &&
            Objects.equals(approvedId, that.approvedId) &&
            Objects.equals(comment, that.comment) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(createById, that.createById) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedById, that.lastModifiedById) &&
            Objects.equals(svcSchedulePlanRecordId, that.svcSchedulePlanRecordId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            svcScheduleUnitId,
            isPublished,
            status,
            approved,
            approvedId,
            comment,
            createDate,
            lastModifiedDate,
            createBy,
            createById,
            lastModifiedBy,
            lastModifiedById,
            svcSchedulePlanRecordId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcScheduleUnitRecordCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (svcScheduleUnitId != null ? "svcScheduleUnitId=" + svcScheduleUnitId + ", " : "") +
            (isPublished != null ? "isPublished=" + isPublished + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (approved != null ? "approved=" + approved + ", " : "") +
            (approvedId != null ? "approvedId=" + approvedId + ", " : "") +
            (comment != null ? "comment=" + comment + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (lastModifiedDate != null ? "lastModifiedDate=" + lastModifiedDate + ", " : "") +
            (createBy != null ? "createBy=" + createBy + ", " : "") +
            (createById != null ? "createById=" + createById + ", " : "") +
            (lastModifiedBy != null ? "lastModifiedBy=" + lastModifiedBy + ", " : "") +
            (lastModifiedById != null ? "lastModifiedById=" + lastModifiedById + ", " : "") +
            (svcSchedulePlanRecordId != null ? "svcSchedulePlanRecordId=" + svcSchedulePlanRecordId + ", " : "") +
            "}";
    }
}
