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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.CoreSupplies} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.CoreSuppliesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /core-supplies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CoreSuppliesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter unit;

    private StringFilter effort;

    private LongFilter coreTaskId;

    public CoreSuppliesCriteria() {}

    public CoreSuppliesCriteria(CoreSuppliesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.effort = other.effort == null ? null : other.effort.copy();
        this.coreTaskId = other.coreTaskId == null ? null : other.coreTaskId.copy();
    }

    @Override
    public CoreSuppliesCriteria copy() {
        return new CoreSuppliesCriteria(this);
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

    public StringFilter getUnit() {
        return unit;
    }

    public StringFilter unit() {
        if (unit == null) {
            unit = new StringFilter();
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public StringFilter getEffort() {
        return effort;
    }

    public StringFilter effort() {
        if (effort == null) {
            effort = new StringFilter();
        }
        return effort;
    }

    public void setEffort(StringFilter effort) {
        this.effort = effort;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoreSuppliesCriteria that = (CoreSuppliesCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(effort, that.effort) &&
            Objects.equals(coreTaskId, that.coreTaskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unit, effort, coreTaskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreSuppliesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (effort != null ? "effort=" + effort + ", " : "") +
            (coreTaskId != null ? "coreTaskId=" + coreTaskId + ", " : "") +
            "}";
    }
}
