package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcScheduleUnitRecord.
 */
@Entity
@Table(name = "svc_schedule_unit_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcScheduleUnitRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "svc_schedule_unit_id", nullable = false)
    private Integer svcScheduleUnitId;

    @NotNull
    @Column(name = "is_published", nullable = false)
    private Integer isPublished;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "approved")
    private String approved;

    @Column(name = "approved_id")
    private Integer approvedId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "last_modified_date")
    private LocalDate lastModifiedDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "create_by_id")
    private Integer createById;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_by_id")
    private Integer lastModifiedById;

    @ManyToMany(mappedBy = "svcScheduleUnitRecords")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "svcScheduleUnitRecords" }, allowSetters = true)
    private Set<SvcSchedulePlanRecord> svcSchedulePlanRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcScheduleUnitRecord id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSvcScheduleUnitId() {
        return this.svcScheduleUnitId;
    }

    public SvcScheduleUnitRecord svcScheduleUnitId(Integer svcScheduleUnitId) {
        this.svcScheduleUnitId = svcScheduleUnitId;
        return this;
    }

    public void setSvcScheduleUnitId(Integer svcScheduleUnitId) {
        this.svcScheduleUnitId = svcScheduleUnitId;
    }

    public Integer getIsPublished() {
        return this.isPublished;
    }

    public SvcScheduleUnitRecord isPublished(Integer isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public String getStatus() {
        return this.status;
    }

    public SvcScheduleUnitRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproved() {
        return this.approved;
    }

    public SvcScheduleUnitRecord approved(String approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Integer getApprovedId() {
        return this.approvedId;
    }

    public SvcScheduleUnitRecord approvedId(Integer approvedId) {
        this.approvedId = approvedId;
        return this;
    }

    public void setApprovedId(Integer approvedId) {
        this.approvedId = approvedId;
    }

    public String getComment() {
        return this.comment;
    }

    public SvcScheduleUnitRecord comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SvcScheduleUnitRecord createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SvcScheduleUnitRecord lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public SvcScheduleUnitRecord createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getCreateById() {
        return this.createById;
    }

    public SvcScheduleUnitRecord createById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SvcScheduleUnitRecord lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getLastModifiedById() {
        return this.lastModifiedById;
    }

    public SvcScheduleUnitRecord lastModifiedById(Integer lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
        return this;
    }

    public void setLastModifiedById(Integer lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Set<SvcSchedulePlanRecord> getSvcSchedulePlanRecords() {
        return this.svcSchedulePlanRecords;
    }

    public SvcScheduleUnitRecord svcSchedulePlanRecords(Set<SvcSchedulePlanRecord> svcSchedulePlanRecords) {
        this.setSvcSchedulePlanRecords(svcSchedulePlanRecords);
        return this;
    }

    public SvcScheduleUnitRecord addSvcSchedulePlanRecord(SvcSchedulePlanRecord svcSchedulePlanRecord) {
        this.svcSchedulePlanRecords.add(svcSchedulePlanRecord);
        svcSchedulePlanRecord.getSvcScheduleUnitRecords().add(this);
        return this;
    }

    public SvcScheduleUnitRecord removeSvcSchedulePlanRecord(SvcSchedulePlanRecord svcSchedulePlanRecord) {
        this.svcSchedulePlanRecords.remove(svcSchedulePlanRecord);
        svcSchedulePlanRecord.getSvcScheduleUnitRecords().remove(this);
        return this;
    }

    public void setSvcSchedulePlanRecords(Set<SvcSchedulePlanRecord> svcSchedulePlanRecords) {
        if (this.svcSchedulePlanRecords != null) {
            this.svcSchedulePlanRecords.forEach(i -> i.removeSvcScheduleUnitRecord(this));
        }
        if (svcSchedulePlanRecords != null) {
            svcSchedulePlanRecords.forEach(i -> i.addSvcScheduleUnitRecord(this));
        }
        this.svcSchedulePlanRecords = svcSchedulePlanRecords;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcScheduleUnitRecord)) {
            return false;
        }
        return id != null && id.equals(((SvcScheduleUnitRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcScheduleUnitRecord{" +
            "id=" + getId() +
            ", svcScheduleUnitId=" + getSvcScheduleUnitId() +
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
            "}";
    }
}
