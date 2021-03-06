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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcGroupTask} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcGroupTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-group-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcGroupTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter createDate;

    private StringFilter createBy;

    private StringFilter category;

    private LongFilter svcAreaId;

    private LongFilter svcSpendTaskId;

    public SvcGroupTaskCriteria() {}

    public SvcGroupTaskCriteria(SvcGroupTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.createBy = other.createBy == null ? null : other.createBy.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.svcAreaId = other.svcAreaId == null ? null : other.svcAreaId.copy();
        this.svcSpendTaskId = other.svcSpendTaskId == null ? null : other.svcSpendTaskId.copy();
    }

    @Override
    public SvcGroupTaskCriteria copy() {
        return new SvcGroupTaskCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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

    public StringFilter getCategory() {
        return category;
    }

    public StringFilter category() {
        if (category == null) {
            category = new StringFilter();
        }
        return category;
    }

    public void setCategory(StringFilter category) {
        this.category = category;
    }

    public LongFilter getSvcAreaId() {
        return svcAreaId;
    }

    public LongFilter svcAreaId() {
        if (svcAreaId == null) {
            svcAreaId = new LongFilter();
        }
        return svcAreaId;
    }

    public void setSvcAreaId(LongFilter svcAreaId) {
        this.svcAreaId = svcAreaId;
    }

    public LongFilter getSvcSpendTaskId() {
        return svcSpendTaskId;
    }

    public LongFilter svcSpendTaskId() {
        if (svcSpendTaskId == null) {
            svcSpendTaskId = new LongFilter();
        }
        return svcSpendTaskId;
    }

    public void setSvcSpendTaskId(LongFilter svcSpendTaskId) {
        this.svcSpendTaskId = svcSpendTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcGroupTaskCriteria that = (SvcGroupTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(createBy, that.createBy) &&
            Objects.equals(category, that.category) &&
            Objects.equals(svcAreaId, that.svcAreaId) &&
            Objects.equals(svcSpendTaskId, that.svcSpendTaskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createDate, createBy, category, svcAreaId, svcSpendTaskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcGroupTaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (createBy != null ? "createBy=" + createBy + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (svcAreaId != null ? "svcAreaId=" + svcAreaId + ", " : "") +
            (svcSpendTaskId != null ? "svcSpendTaskId=" + svcSpendTaskId + ", " : "") +
            "}";
    }
}
