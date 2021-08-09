package com.overnetcontact.dvvs.service.criteria;

import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.overnetcontact.dvvs.domain.SvcContract} entity. This class is used
 * in {@link com.overnetcontact.dvvs.web.rest.SvcContractResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /svc-contracts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SvcContractCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SvcContractStatus
     */
    public static class SvcContractStatusFilter extends Filter<SvcContractStatus> {

        public SvcContractStatusFilter() {}

        public SvcContractStatusFilter(SvcContractStatusFilter filter) {
            super(filter);
        }

        @Override
        public SvcContractStatusFilter copy() {
            return new SvcContractStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter orderNumber;

    private StringFilter documentId;

    private StringFilter appendicesNumber;

    private StringFilter fileId;

    private StringFilter content;

    private InstantFilter effectiveTimeFrom;

    private InstantFilter effectiveTimeTo;

    private IntegerFilter durationMonth;

    private BigDecimalFilter value;

    private BigDecimalFilter contractValue;

    private IntegerFilter humanResources;

    private IntegerFilter humanResourcesWeekend;

    private SvcContractStatusFilter status;

    private LongFilter subjectCount;

    private BigDecimalFilter valuePerPerson;

    private IntegerFilter year;

    private LongFilter targetsId;

    private LongFilter approvedById;

    private LongFilter ownerById;

    private LongFilter unitId;

    private LongFilter salerId;

    private LongFilter clientId;

    public SvcContractCriteria() {}

    public SvcContractCriteria(SvcContractCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.orderNumber = other.orderNumber == null ? null : other.orderNumber.copy();
        this.documentId = other.documentId == null ? null : other.documentId.copy();
        this.appendicesNumber = other.appendicesNumber == null ? null : other.appendicesNumber.copy();
        this.fileId = other.fileId == null ? null : other.fileId.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.effectiveTimeFrom = other.effectiveTimeFrom == null ? null : other.effectiveTimeFrom.copy();
        this.effectiveTimeTo = other.effectiveTimeTo == null ? null : other.effectiveTimeTo.copy();
        this.durationMonth = other.durationMonth == null ? null : other.durationMonth.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.contractValue = other.contractValue == null ? null : other.contractValue.copy();
        this.humanResources = other.humanResources == null ? null : other.humanResources.copy();
        this.humanResourcesWeekend = other.humanResourcesWeekend == null ? null : other.humanResourcesWeekend.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.subjectCount = other.subjectCount == null ? null : other.subjectCount.copy();
        this.valuePerPerson = other.valuePerPerson == null ? null : other.valuePerPerson.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.targetsId = other.targetsId == null ? null : other.targetsId.copy();
        this.approvedById = other.approvedById == null ? null : other.approvedById.copy();
        this.ownerById = other.ownerById == null ? null : other.ownerById.copy();
        this.unitId = other.unitId == null ? null : other.unitId.copy();
        this.salerId = other.salerId == null ? null : other.salerId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
    }

    @Override
    public SvcContractCriteria copy() {
        return new SvcContractCriteria(this);
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

    public LongFilter getOrderNumber() {
        return orderNumber;
    }

    public LongFilter orderNumber() {
        if (orderNumber == null) {
            orderNumber = new LongFilter();
        }
        return orderNumber;
    }

    public void setOrderNumber(LongFilter orderNumber) {
        this.orderNumber = orderNumber;
    }

    public StringFilter getDocumentId() {
        return documentId;
    }

    public StringFilter documentId() {
        if (documentId == null) {
            documentId = new StringFilter();
        }
        return documentId;
    }

    public void setDocumentId(StringFilter documentId) {
        this.documentId = documentId;
    }

    public StringFilter getAppendicesNumber() {
        return appendicesNumber;
    }

    public StringFilter appendicesNumber() {
        if (appendicesNumber == null) {
            appendicesNumber = new StringFilter();
        }
        return appendicesNumber;
    }

    public void setAppendicesNumber(StringFilter appendicesNumber) {
        this.appendicesNumber = appendicesNumber;
    }

    public StringFilter getFileId() {
        return fileId;
    }

    public StringFilter fileId() {
        if (fileId == null) {
            fileId = new StringFilter();
        }
        return fileId;
    }

    public void setFileId(StringFilter fileId) {
        this.fileId = fileId;
    }

    public StringFilter getContent() {
        return content;
    }

    public StringFilter content() {
        if (content == null) {
            content = new StringFilter();
        }
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public InstantFilter getEffectiveTimeFrom() {
        return effectiveTimeFrom;
    }

    public InstantFilter effectiveTimeFrom() {
        if (effectiveTimeFrom == null) {
            effectiveTimeFrom = new InstantFilter();
        }
        return effectiveTimeFrom;
    }

    public void setEffectiveTimeFrom(InstantFilter effectiveTimeFrom) {
        this.effectiveTimeFrom = effectiveTimeFrom;
    }

    public InstantFilter getEffectiveTimeTo() {
        return effectiveTimeTo;
    }

    public InstantFilter effectiveTimeTo() {
        if (effectiveTimeTo == null) {
            effectiveTimeTo = new InstantFilter();
        }
        return effectiveTimeTo;
    }

    public void setEffectiveTimeTo(InstantFilter effectiveTimeTo) {
        this.effectiveTimeTo = effectiveTimeTo;
    }

    public IntegerFilter getDurationMonth() {
        return durationMonth;
    }

    public IntegerFilter durationMonth() {
        if (durationMonth == null) {
            durationMonth = new IntegerFilter();
        }
        return durationMonth;
    }

    public void setDurationMonth(IntegerFilter durationMonth) {
        this.durationMonth = durationMonth;
    }

    public BigDecimalFilter getValue() {
        return value;
    }

    public BigDecimalFilter value() {
        if (value == null) {
            value = new BigDecimalFilter();
        }
        return value;
    }

    public void setValue(BigDecimalFilter value) {
        this.value = value;
    }

    public BigDecimalFilter getContractValue() {
        return contractValue;
    }

    public BigDecimalFilter contractValue() {
        if (contractValue == null) {
            contractValue = new BigDecimalFilter();
        }
        return contractValue;
    }

    public void setContractValue(BigDecimalFilter contractValue) {
        this.contractValue = contractValue;
    }

    public IntegerFilter getHumanResources() {
        return humanResources;
    }

    public IntegerFilter humanResources() {
        if (humanResources == null) {
            humanResources = new IntegerFilter();
        }
        return humanResources;
    }

    public void setHumanResources(IntegerFilter humanResources) {
        this.humanResources = humanResources;
    }

    public IntegerFilter getHumanResourcesWeekend() {
        return humanResourcesWeekend;
    }

    public IntegerFilter humanResourcesWeekend() {
        if (humanResourcesWeekend == null) {
            humanResourcesWeekend = new IntegerFilter();
        }
        return humanResourcesWeekend;
    }

    public void setHumanResourcesWeekend(IntegerFilter humanResourcesWeekend) {
        this.humanResourcesWeekend = humanResourcesWeekend;
    }

    public SvcContractStatusFilter getStatus() {
        return status;
    }

    public SvcContractStatusFilter status() {
        if (status == null) {
            status = new SvcContractStatusFilter();
        }
        return status;
    }

    public void setStatus(SvcContractStatusFilter status) {
        this.status = status;
    }

    public LongFilter getSubjectCount() {
        return subjectCount;
    }

    public LongFilter subjectCount() {
        if (subjectCount == null) {
            subjectCount = new LongFilter();
        }
        return subjectCount;
    }

    public void setSubjectCount(LongFilter subjectCount) {
        this.subjectCount = subjectCount;
    }

    public BigDecimalFilter getValuePerPerson() {
        return valuePerPerson;
    }

    public BigDecimalFilter valuePerPerson() {
        if (valuePerPerson == null) {
            valuePerPerson = new BigDecimalFilter();
        }
        return valuePerPerson;
    }

    public void setValuePerPerson(BigDecimalFilter valuePerPerson) {
        this.valuePerPerson = valuePerPerson;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public IntegerFilter year() {
        if (year == null) {
            year = new IntegerFilter();
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public LongFilter getTargetsId() {
        return targetsId;
    }

    public LongFilter targetsId() {
        if (targetsId == null) {
            targetsId = new LongFilter();
        }
        return targetsId;
    }

    public void setTargetsId(LongFilter targetsId) {
        this.targetsId = targetsId;
    }

    public LongFilter getApprovedById() {
        return approvedById;
    }

    public LongFilter approvedById() {
        if (approvedById == null) {
            approvedById = new LongFilter();
        }
        return approvedById;
    }

    public void setApprovedById(LongFilter approvedById) {
        this.approvedById = approvedById;
    }

    public LongFilter getOwnerById() {
        return ownerById;
    }

    public LongFilter ownerById() {
        if (ownerById == null) {
            ownerById = new LongFilter();
        }
        return ownerById;
    }

    public void setOwnerById(LongFilter ownerById) {
        this.ownerById = ownerById;
    }

    public LongFilter getUnitId() {
        return unitId;
    }

    public LongFilter unitId() {
        if (unitId == null) {
            unitId = new LongFilter();
        }
        return unitId;
    }

    public void setUnitId(LongFilter unitId) {
        this.unitId = unitId;
    }

    public LongFilter getSalerId() {
        return salerId;
    }

    public LongFilter salerId() {
        if (salerId == null) {
            salerId = new LongFilter();
        }
        return salerId;
    }

    public void setSalerId(LongFilter salerId) {
        this.salerId = salerId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SvcContractCriteria that = (SvcContractCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(documentId, that.documentId) &&
            Objects.equals(appendicesNumber, that.appendicesNumber) &&
            Objects.equals(fileId, that.fileId) &&
            Objects.equals(content, that.content) &&
            Objects.equals(effectiveTimeFrom, that.effectiveTimeFrom) &&
            Objects.equals(effectiveTimeTo, that.effectiveTimeTo) &&
            Objects.equals(durationMonth, that.durationMonth) &&
            Objects.equals(value, that.value) &&
            Objects.equals(contractValue, that.contractValue) &&
            Objects.equals(humanResources, that.humanResources) &&
            Objects.equals(humanResourcesWeekend, that.humanResourcesWeekend) &&
            Objects.equals(status, that.status) &&
            Objects.equals(subjectCount, that.subjectCount) &&
            Objects.equals(valuePerPerson, that.valuePerPerson) &&
            Objects.equals(year, that.year) &&
            Objects.equals(targetsId, that.targetsId) &&
            Objects.equals(approvedById, that.approvedById) &&
            Objects.equals(ownerById, that.ownerById) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(salerId, that.salerId) &&
            Objects.equals(clientId, that.clientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            orderNumber,
            documentId,
            appendicesNumber,
            fileId,
            content,
            effectiveTimeFrom,
            effectiveTimeTo,
            durationMonth,
            value,
            contractValue,
            humanResources,
            humanResourcesWeekend,
            status,
            subjectCount,
            valuePerPerson,
            year,
            targetsId,
            approvedById,
            ownerById,
            unitId,
            salerId,
            clientId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcContractCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (orderNumber != null ? "orderNumber=" + orderNumber + ", " : "") +
            (documentId != null ? "documentId=" + documentId + ", " : "") +
            (appendicesNumber != null ? "appendicesNumber=" + appendicesNumber + ", " : "") +
            (fileId != null ? "fileId=" + fileId + ", " : "") +
            (content != null ? "content=" + content + ", " : "") +
            (effectiveTimeFrom != null ? "effectiveTimeFrom=" + effectiveTimeFrom + ", " : "") +
            (effectiveTimeTo != null ? "effectiveTimeTo=" + effectiveTimeTo + ", " : "") +
            (durationMonth != null ? "durationMonth=" + durationMonth + ", " : "") +
            (value != null ? "value=" + value + ", " : "") +
            (contractValue != null ? "contractValue=" + contractValue + ", " : "") +
            (humanResources != null ? "humanResources=" + humanResources + ", " : "") +
            (humanResourcesWeekend != null ? "humanResourcesWeekend=" + humanResourcesWeekend + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (subjectCount != null ? "subjectCount=" + subjectCount + ", " : "") +
            (valuePerPerson != null ? "valuePerPerson=" + valuePerPerson + ", " : "") +
            (year != null ? "year=" + year + ", " : "") +
            (targetsId != null ? "targetsId=" + targetsId + ", " : "") +
            (approvedById != null ? "approvedById=" + approvedById + ", " : "") +
            (ownerById != null ? "ownerById=" + ownerById + ", " : "") +
            (unitId != null ? "unitId=" + unitId + ", " : "") +
            (salerId != null ? "salerId=" + salerId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }
}
