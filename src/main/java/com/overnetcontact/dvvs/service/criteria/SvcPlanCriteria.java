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
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcPlan} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcPlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcPlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter serviceManagerId;

    private LongFilter defaultSuppervisorId;

    private BooleanFilter status;

    private LocalDateFilter startPlan;

    private LocalDateFilter endPlan;

    private LocalDateFilter createDate;

    private LongFilter contractId;

    private StringFilter note;

    private LongFilter svcPlanUnitId;

    public SvcPlanCriteria() {}

    public SvcPlanCriteria(SvcPlanCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.serviceManagerId = other.serviceManagerId == null ? null : other.serviceManagerId.copy();
        this.defaultSuppervisorId = other.defaultSuppervisorId == null ? null : other.defaultSuppervisorId.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.startPlan = other.startPlan == null ? null : other.startPlan.copy();
        this.endPlan = other.endPlan == null ? null : other.endPlan.copy();
        this.createDate = other.createDate == null ? null : other.createDate.copy();
        this.contractId = other.contractId == null ? null : other.contractId.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.svcPlanUnitId = other.svcPlanUnitId == null ? null : other.svcPlanUnitId.copy();
    }

    @Override
    public SvcPlanCriteria copy() {
        return new SvcPlanCriteria(this);
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

    public LongFilter getServiceManagerId() {
        return serviceManagerId;
    }

    public LongFilter serviceManagerId() {
        if (serviceManagerId == null) {
            serviceManagerId = new LongFilter();
        }
        return serviceManagerId;
    }

    public void setServiceManagerId(LongFilter serviceManagerId) {
        this.serviceManagerId = serviceManagerId;
    }

    public LongFilter getDefaultSuppervisorId() {
        return defaultSuppervisorId;
    }

    public LongFilter defaultSuppervisorId() {
        if (defaultSuppervisorId == null) {
            defaultSuppervisorId = new LongFilter();
        }
        return defaultSuppervisorId;
    }

    public void setDefaultSuppervisorId(LongFilter defaultSuppervisorId) {
        this.defaultSuppervisorId = defaultSuppervisorId;
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

    public LocalDateFilter getStartPlan() {
        return startPlan;
    }

    public LocalDateFilter startPlan() {
        if (startPlan == null) {
            startPlan = new LocalDateFilter();
        }
        return startPlan;
    }

    public void setStartPlan(LocalDateFilter startPlan) {
        this.startPlan = startPlan;
    }

    public LocalDateFilter getEndPlan() {
        return endPlan;
    }

    public LocalDateFilter endPlan() {
        if (endPlan == null) {
            endPlan = new LocalDateFilter();
        }
        return endPlan;
    }

    public void setEndPlan(LocalDateFilter endPlan) {
        this.endPlan = endPlan;
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

    public LongFilter getContractId() {
        return contractId;
    }

    public LongFilter contractId() {
        if (contractId == null) {
            contractId = new LongFilter();
        }
        return contractId;
    }

    public void setContractId(LongFilter contractId) {
        this.contractId = contractId;
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
        final SvcPlanCriteria that = (SvcPlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(serviceManagerId, that.serviceManagerId) &&
            Objects.equals(defaultSuppervisorId, that.defaultSuppervisorId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startPlan, that.startPlan) &&
            Objects.equals(endPlan, that.endPlan) &&
            Objects.equals(createDate, that.createDate) &&
            Objects.equals(contractId, that.contractId) &&
            Objects.equals(note, that.note) &&
            Objects.equals(svcPlanUnitId, that.svcPlanUnitId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            serviceManagerId,
            defaultSuppervisorId,
            status,
            startPlan,
            endPlan,
            createDate,
            contractId,
            note,
            svcPlanUnitId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcPlanCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (serviceManagerId != null ? "serviceManagerId=" + serviceManagerId + ", " : "") +
            (defaultSuppervisorId != null ? "defaultSuppervisorId=" + defaultSuppervisorId + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (startPlan != null ? "startPlan=" + startPlan + ", " : "") +
            (endPlan != null ? "endPlan=" + endPlan + ", " : "") +
            (createDate != null ? "createDate=" + createDate + ", " : "") +
            (contractId != null ? "contractId=" + contractId + ", " : "") +
            (note != null ? "note=" + note + ", " : "") +
            (svcPlanUnitId != null ? "svcPlanUnitId=" + svcPlanUnitId + ", " : "") +
            "}";
    }
}
