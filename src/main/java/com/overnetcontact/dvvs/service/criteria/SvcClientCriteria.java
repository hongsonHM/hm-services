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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcClient} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter customerName;

    private StringFilter customerCity;

    private StringFilter phoneNumber;

    private StringFilter type;

    private StringFilter address;

    public SvcClientCriteria() {}

    public SvcClientCriteria(SvcClientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerName = other.customerName == null ? null : other.customerName.copy();
        this.customerCity = other.customerCity == null ? null : other.customerCity.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.address = other.address == null ? null : other.address.copy();
    }

    @Override
    public SvcClientCriteria copy() {
        return new SvcClientCriteria(this);
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

    public StringFilter getCustomerName() {
        return customerName;
    }

    public StringFilter customerName() {
        if (customerName == null) {
            customerName = new StringFilter();
        }
        return customerName;
    }

    public void setCustomerName(StringFilter customerName) {
        this.customerName = customerName;
    }

    public StringFilter getCustomerCity() {
        return customerCity;
    }

    public StringFilter customerCity() {
        if (customerCity == null) {
            customerCity = new StringFilter();
        }
        return customerCity;
    }

    public void setCustomerCity(StringFilter customerCity) {
        this.customerCity = customerCity;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcClientCriteria that = (SvcClientCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerName, that.customerName) &&
            Objects.equals(customerCity, that.customerCity) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(type, that.type) &&
            Objects.equals(address, that.address)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, customerCity, phoneNumber, type, address);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcClientCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerName != null ? "customerName=" + customerName + ", " : "") +
            (customerCity != null ? "customerCity=" + customerCity + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            "}";
    }
}
