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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcLabor} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcLaborResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-labors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcLaborCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter phone;

    private StringFilter address;

    private StringFilter laborCode;

    private LongFilter svcPlanUnitId;

    public SvcLaborCriteria() {}

    public SvcLaborCriteria(SvcLaborCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.laborCode = other.laborCode == null ? null : other.laborCode.copy();
        this.svcPlanUnitId = other.svcPlanUnitId == null ? null : other.svcPlanUnitId.copy();
    }

    @Override
    public SvcLaborCriteria copy() {
        return new SvcLaborCriteria(this);
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

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getLaborCode() {
        return laborCode;
    }

    public StringFilter laborCode() {
        if (laborCode == null) {
            laborCode = new StringFilter();
        }
        return laborCode;
    }

    public void setLaborCode(StringFilter laborCode) {
        this.laborCode = laborCode;
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
        final SvcLaborCriteria that = (SvcLaborCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(address, that.address) &&
            Objects.equals(laborCode, that.laborCode) &&
            Objects.equals(svcPlanUnitId, that.svcPlanUnitId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone, address, laborCode, svcPlanUnitId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcLaborCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (laborCode != null ? "laborCode=" + laborCode + ", " : "") +
            (svcPlanUnitId != null ? "svcPlanUnitId=" + svcPlanUnitId + ", " : "") +
            "}";
    }
}
