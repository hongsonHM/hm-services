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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.CoreTask} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.CoreTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /core-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CoreTaskCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter unit;

    private StringFilter category;

    private StringFilter note;

    private LongFilter coreSuppliesId;

    public CoreTaskCriteria() {}

    public CoreTaskCriteria(CoreTaskCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.unit = other.unit == null ? null : other.unit.copy();
        this.category = other.category == null ? null : other.category.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.coreSuppliesId = other.coreSuppliesId == null ? null : other.coreSuppliesId.copy();
    }

    @Override
    public CoreTaskCriteria copy() {
        return new CoreTaskCriteria(this);
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

    public LongFilter getCoreSuppliesId() {
        return coreSuppliesId;
    }

    public LongFilter coreSuppliesId() {
        if (coreSuppliesId == null) {
            coreSuppliesId = new LongFilter();
        }
        return coreSuppliesId;
    }

    public void setCoreSuppliesId(LongFilter coreSuppliesId) {
        this.coreSuppliesId = coreSuppliesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CoreTaskCriteria that = (CoreTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(category, that.category) &&
            Objects.equals(note, that.note) &&
            Objects.equals(coreSuppliesId, that.coreSuppliesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, unit, category, note, coreSuppliesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreTaskCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (unit != null ? "unit=" + unit + ", " : "") +
            (category != null ? "category=" + category + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (coreSuppliesId != null ? "coreSuppliesId=" + coreSuppliesId + ", " : "") +
            "}";
    }
}
