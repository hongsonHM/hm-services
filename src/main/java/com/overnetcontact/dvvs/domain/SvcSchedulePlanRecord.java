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
 * A SvcSchedulePlanRecord.
 */
@Entity
@Table(name = "svc_schedule_plan_record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcSchedulePlanRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "svc_schedule_plan_id", nullable = false)
    private Integer svcSchedulePlanId;

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

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_svc_schedule_plan_record__svc_schedule_unit_record",
        joinColumns = @JoinColumn(name = "svc_schedule_plan_record_id"),
        inverseJoinColumns = @JoinColumn(name = "svc_schedule_unit_record_id")
    )
    @JsonIgnoreProperties(value = { "svcSchedulePlanRecords" }, allowSetters = true)
    private Set<SvcScheduleUnitRecord> svcScheduleUnitRecords = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcSchedulePlanRecord id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getSvcSchedulePlanId() {
        return this.svcSchedulePlanId;
    }

    public SvcSchedulePlanRecord svcSchedulePlanId(Integer svcSchedulePlanId) {
        this.svcSchedulePlanId = svcSchedulePlanId;
        return this;
    }

    public void setSvcSchedulePlanId(Integer svcSchedulePlanId) {
        this.svcSchedulePlanId = svcSchedulePlanId;
    }

    public Integer getIsPublished() {
        return this.isPublished;
    }

    public SvcSchedulePlanRecord isPublished(Integer isPublished) {
        this.isPublished = isPublished;
        return this;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public String getStatus() {
        return this.status;
    }

    public SvcSchedulePlanRecord status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproved() {
        return this.approved;
    }

    public SvcSchedulePlanRecord approved(String approved) {
        this.approved = approved;
        return this;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public Integer getApprovedId() {
        return this.approvedId;
    }

    public SvcSchedulePlanRecord approvedId(Integer approvedId) {
        this.approvedId = approvedId;
        return this;
    }

    public void setApprovedId(Integer approvedId) {
        this.approvedId = approvedId;
    }

    public String getComment() {
        return this.comment;
    }

    public SvcSchedulePlanRecord comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SvcSchedulePlanRecord createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public SvcSchedulePlanRecord lastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public SvcSchedulePlanRecord createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Integer getCreateById() {
        return this.createById;
    }

    public SvcSchedulePlanRecord createById(Integer createById) {
        this.createById = createById;
        return this;
    }

    public void setCreateById(Integer createById) {
        this.createById = createById;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public SvcSchedulePlanRecord lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Integer getLastModifiedById() {
        return this.lastModifiedById;
    }

    public SvcSchedulePlanRecord lastModifiedById(Integer lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
        return this;
    }

    public void setLastModifiedById(Integer lastModifiedById) {
        this.lastModifiedById = lastModifiedById;
    }

    public Set<SvcScheduleUnitRecord> getSvcScheduleUnitRecords() {
        return this.svcScheduleUnitRecords;
    }

    public SvcSchedulePlanRecord svcScheduleUnitRecords(Set<SvcScheduleUnitRecord> svcScheduleUnitRecords) {
        this.setSvcScheduleUnitRecords(svcScheduleUnitRecords);
        return this;
    }

    public SvcSchedulePlanRecord addSvcScheduleUnitRecord(SvcScheduleUnitRecord svcScheduleUnitRecord) {
        this.svcScheduleUnitRecords.add(svcScheduleUnitRecord);
        svcScheduleUnitRecord.getSvcSchedulePlanRecords().add(this);
        return this;
    }

    public SvcSchedulePlanRecord removeSvcScheduleUnitRecord(SvcScheduleUnitRecord svcScheduleUnitRecord) {
        this.svcScheduleUnitRecords.remove(svcScheduleUnitRecord);
        svcScheduleUnitRecord.getSvcSchedulePlanRecords().remove(this);
        return this;
    }

    public void setSvcScheduleUnitRecords(Set<SvcScheduleUnitRecord> svcScheduleUnitRecords) {
        this.svcScheduleUnitRecords = svcScheduleUnitRecords;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcSchedulePlanRecord)) {
            return false;
        }
        return id != null && id.equals(((SvcSchedulePlanRecord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcSchedulePlanRecord{" +
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
            "}";
    }
}
