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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcTarget} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcTargetResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-targets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcTargetCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter childsId;

    private LongFilter typeId;

    private LongFilter svcTargetId;

    private LongFilter svcContractId;

    public SvcTargetCriteria() {}

    public SvcTargetCriteria(SvcTargetCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.childsId = other.childsId == null ? null : other.childsId.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.svcTargetId = other.svcTargetId == null ? null : other.svcTargetId.copy();
        this.svcContractId = other.svcContractId == null ? null : other.svcContractId.copy();
    }

    @Override
    public SvcTargetCriteria copy() {
        return new SvcTargetCriteria(this);
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

    public LongFilter getChildsId() {
        return childsId;
    }

    public LongFilter childsId() {
        if (childsId == null) {
            childsId = new LongFilter();
        }
        return childsId;
    }

    public void setChildsId(LongFilter childsId) {
        this.childsId = childsId;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public LongFilter typeId() {
        if (typeId == null) {
            typeId = new LongFilter();
        }
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getSvcTargetId() {
        return svcTargetId;
    }

    public LongFilter svcTargetId() {
        if (svcTargetId == null) {
            svcTargetId = new LongFilter();
        }
        return svcTargetId;
    }

    public void setSvcTargetId(LongFilter svcTargetId) {
        this.svcTargetId = svcTargetId;
    }

    public LongFilter getSvcContractId() {
        return svcContractId;
    }

    public LongFilter svcContractId() {
        if (svcContractId == null) {
            svcContractId = new LongFilter();
        }
        return svcContractId;
    }

    public void setSvcContractId(LongFilter svcContractId) {
        this.svcContractId = svcContractId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcTargetCriteria that = (SvcTargetCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(childsId, that.childsId) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(svcTargetId, that.svcTargetId) &&
            Objects.equals(svcContractId, that.svcContractId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, childsId, typeId, svcTargetId, svcContractId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcTargetCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (childsId != null ? "childsId=" + childsId + ", " : "") +
            (typeId != null ? "typeId=" + typeId + ", " : "") +
            (svcTargetId != null ? "svcTargetId=" + svcTargetId + ", " : "") +
            (svcContractId != null ? "svcContractId=" + svcContractId + ", " : "") +
            "}";
    }
}
