package com.overnetcontact.dvvs.service.dto;

import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcContract} entity.
 */
@ApiModel(description = "SvcContract entity of SVC.\n@author KagariV.")
public class SvcContractDTO implements Serializable {

    private Long id;

    @NotNull
    private Long orderNumber;

    private String documentId;

    private String appendicesNumber;

    private String fileId;

    private String content;

    @NotNull
    private Instant effectiveTimeFrom;

    @NotNull
    private Instant effectiveTimeTo;

    @NotNull
    private Integer durationMonth;

    @NotNull
    private BigDecimal value;

    @NotNull
    private BigDecimal contractValue;

    private Integer humanResources;

    private Integer humanResourcesWeekend;

    @NotNull
    private SvcContractStatus status;

    private Long subjectCount;

    private BigDecimal valuePerPerson;

    private Integer year;

    private SvcUnitDTO unit;

    private OrgUserDTO saler;

    private SvcClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAppendicesNumber() {
        return appendicesNumber;
    }

    public void setAppendicesNumber(String appendicesNumber) {
        this.appendicesNumber = appendicesNumber;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getEffectiveTimeFrom() {
        return effectiveTimeFrom;
    }

    public void setEffectiveTimeFrom(Instant effectiveTimeFrom) {
        this.effectiveTimeFrom = effectiveTimeFrom;
    }

    public Instant getEffectiveTimeTo() {
        return effectiveTimeTo;
    }

    public void setEffectiveTimeTo(Instant effectiveTimeTo) {
        this.effectiveTimeTo = effectiveTimeTo;
    }

    public Integer getDurationMonth() {
        return durationMonth;
    }

    public void setDurationMonth(Integer durationMonth) {
        this.durationMonth = durationMonth;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getContractValue() {
        return contractValue;
    }

    public void setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
    }

    public Integer getHumanResources() {
        return humanResources;
    }

    public void setHumanResources(Integer humanResources) {
        this.humanResources = humanResources;
    }

    public Integer getHumanResourcesWeekend() {
        return humanResourcesWeekend;
    }

    public void setHumanResourcesWeekend(Integer humanResourcesWeekend) {
        this.humanResourcesWeekend = humanResourcesWeekend;
    }

    public SvcContractStatus getStatus() {
        return status;
    }

    public void setStatus(SvcContractStatus status) {
        this.status = status;
    }

    public Long getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(Long subjectCount) {
        this.subjectCount = subjectCount;
    }

    public BigDecimal getValuePerPerson() {
        return valuePerPerson;
    }

    public void setValuePerPerson(BigDecimal valuePerPerson) {
        this.valuePerPerson = valuePerPerson;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SvcUnitDTO getUnit() {
        return unit;
    }

    public void setUnit(SvcUnitDTO unit) {
        this.unit = unit;
    }

    public OrgUserDTO getSaler() {
        return saler;
    }

    public void setSaler(OrgUserDTO saler) {
        this.saler = saler;
    }

    public SvcClientDTO getClient() {
        return client;
    }

    public void setClient(SvcClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcContractDTO)) {
            return false;
        }

        SvcContractDTO svcContractDTO = (SvcContractDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcContractDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcContractDTO{" +
            "id=" + getId() +
            ", orderNumber=" + getOrderNumber() +
            ", documentId='" + getDocumentId() + "'" +
            ", appendicesNumber='" + getAppendicesNumber() + "'" +
            ", fileId='" + getFileId() + "'" +
            ", content='" + getContent() + "'" +
            ", effectiveTimeFrom='" + getEffectiveTimeFrom() + "'" +
            ", effectiveTimeTo='" + getEffectiveTimeTo() + "'" +
            ", durationMonth=" + getDurationMonth() +
            ", value=" + getValue() +
            ", contractValue=" + getContractValue() +
            ", humanResources=" + getHumanResources() +
            ", humanResourcesWeekend=" + getHumanResourcesWeekend() +
            ", status='" + getStatus() + "'" +
            ", subjectCount=" + getSubjectCount() +
            ", valuePerPerson=" + getValuePerPerson() +
            ", year=" + getYear() +
            ", unit=" + getUnit() +
            ", saler=" + getSaler() +
            ", client=" + getClient() +
            "}";
    }
}
