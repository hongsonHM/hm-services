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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcArea} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcAreaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-areas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcAreaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter location;

    private StringFilter type;

    private LongFilter contractsId;

    private LongFilter svcGroupTaskId;

    public SvcAreaCriteria() {}

    public SvcAreaCriteria(SvcAreaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.contractsId = other.contractsId == null ? null : other.contractsId.copy();
        this.svcGroupTaskId = other.svcGroupTaskId == null ? null : other.svcGroupTaskId.copy();
    }

    @Override
    public SvcAreaCriteria copy() {
        return new SvcAreaCriteria(this);
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

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getContractsId() {
        return contractsId;
    }

    public LongFilter contractsId() {
        if (contractsId == null) {
            contractsId = new LongFilter();
        }
        return contractsId;
    }

    public void setContractsId(LongFilter contractsId) {
        this.contractsId = contractsId;
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
        final SvcAreaCriteria that = (SvcAreaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(location, that.location) &&
            Objects.equals(type, that.type) &&
            Objects.equals(contractsId, that.contractsId) &&
            Objects.equals(svcGroupTaskId, that.svcGroupTaskId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, type, contractsId, svcGroupTaskId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcAreaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (contractsId != null ? "contractsId=" + contractsId + ", " : "") +
            (svcGroupTaskId != null ? "svcGroupTaskId=" + svcGroupTaskId + ", " : "") +
            "}";
    }
}
