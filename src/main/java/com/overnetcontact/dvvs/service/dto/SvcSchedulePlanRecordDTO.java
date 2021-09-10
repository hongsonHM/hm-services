package com.overnetcontact.dvvs.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord} entity.
 */
public class SvcSchedulePlanRecordDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer svcSchedulePlanId;

    @NotNull
    private Integer isPublished;

    @NotNull
    private String status;

    private String approved;

    private Integer approvedId;

    private String comment;

    private LocalDate createDate;

    private LocalDate lastModifiedDate;

    private String createBy;

    private Integer createById;

    private String lastModifiedBy;

    private Integer lastModifiedById;

    private Set<SvcScheduleUnitRecordDTO> svcScheduleUnitRecords = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSvcSchedulePlanId() {
        return svcSchedulePlanId;
    }

    public void setSvcSchedulePlanId(Integer svcSchedulePlanId) {
        this.svcSchedulePlanId = svcSchedulePlanId;
    }

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Integer getApprovedId() {
        return approvedId;
    }

    public void setApprovedId(Integer approvedId) {
        this.approvedId = approvedId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getCreateById() {
        return createById;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Integer lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Set<SvcScheduleUnitRecordDTO> getSvcScheduleUnitRecords() {
        return svcScheduleUnitRecords;
    }

    public void setSvcScheduleUnitRecords(Set<SvcScheduleUnitRecordDTO> svcScheduleUnitRecords) {
        this.svcScheduleUnitRecords = svcScheduleUnitRecords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSchedulePlanRecordDTO)) {
            return false;
        }

        SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO = (SvcSchedulePlanRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, svcSchedulePlanRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSchedulePlanRecordDTO{" +
            "id=" + getId() +
            ", svcSchedulePlanId=" + getSvcSchedulePlanId() +
            ", isPublished=" + getIsPublished() +
            ", status='" + getStatus() + "'" +
            ", approved='" + getApproved() + "'" +
            ", approvedId=" + getApprovedId() +
            ", comment='" + getComment() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createById=" + getCreateById() +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedById=" + getLastModifiedById() +
            ", svcScheduleUnitRecords=" + getSvcScheduleUnitRecords() +
            "}";
    }
}
