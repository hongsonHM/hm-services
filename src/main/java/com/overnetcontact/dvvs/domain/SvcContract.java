package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * SvcContract entity of SVC.\n@author KagariV.
 */
@Entity
@Table(name = "svc_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private Long orderNumber;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "appendices_number")
    private String appendicesNumber;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "effective_time_from", nullable = false)
    private Instant effectiveTimeFrom;

    @NotNull
    @Column(name = "effective_time_to", nullable = false)
    private Instant effectiveTimeTo;

    @NotNull
    @Column(name = "duration_month", nullable = false)
    private Integer durationMonth;

    @NotNull
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "contract_value", precision = 21, scale = 2, nullable = false)
    private BigDecimal contractValue;

    @Column(name = "human_resources")
    private Integer humanResources;

    @Column(name = "human_resources_weekend")
    private Integer humanResourcesWeekend;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SvcContractStatus status;

    @Column(name = "subject_count")
    private Long subjectCount;

    @Column(name = "value_per_person", precision = 21, scale = 2)
    private BigDecimal valuePerPerson;

    @Column(name = "year")
    private Integer year;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "dvvs_contract_target",
        joinColumns = { @JoinColumn(name = "contract_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "target_id", referencedColumnName = "id") }
    )
    @JsonIgnoreProperties(value = { "childs", "type", "svcTarget", "svcContract" }, allowSetters = true)
    private Set<SvcTarget> targets = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "dvvs_contract_approved_by",
        joinColumns = { @JoinColumn(name = "contract_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }
    )
    private List<User> approvedBy;

    @ManyToMany
    @JoinTable(
        name = "dvvs_contract_manager_by",
        joinColumns = { @JoinColumn(name = "contract_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }
    )
    private List<User> managerBy;

    @ManyToOne
    private User ownerBy;

    @ManyToMany
    @JoinTable(
        name = "dvvs_contract_group_notification",
        joinColumns = { @JoinColumn(name = "contract_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "org_group_id", referencedColumnName = "id") }
    )
    private List<OrgGroup> notificationUnits;

    @ManyToOne
    @JsonIgnoreProperties(value = { "group" }, allowSetters = true)
    private SvcUnit unit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "notifications", "group" }, allowSetters = true)
    private OrgUser saler;

    @ManyToOne(cascade = CascadeType.ALL)
    private SvcClient client;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcContract id(Long id) {
        this.id = id;
        return this;
    }

    public Long getOrderNumber() {
        return this.orderNumber;
    }

    public SvcContract orderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDocumentId() {
        return this.documentId;
    }

    public SvcContract documentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAppendicesNumber() {
        return this.appendicesNumber;
    }

    public SvcContract appendicesNumber(String appendicesNumber) {
        this.appendicesNumber = appendicesNumber;
        return this;
    }

    public void setAppendicesNumber(String appendicesNumber) {
        this.appendicesNumber = appendicesNumber;
    }

    public String getFileId() {
        return this.fileId;
    }

    public SvcContract fileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getContent() {
        return this.content;
    }

    public SvcContract content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getEffectiveTimeFrom() {
        return this.effectiveTimeFrom;
    }

    public SvcContract effectiveTimeFrom(Instant effectiveTimeFrom) {
        this.effectiveTimeFrom = effectiveTimeFrom;
        return this;
    }

    public void setEffectiveTimeFrom(Instant effectiveTimeFrom) {
        this.effectiveTimeFrom = effectiveTimeFrom;
    }

    public Instant getEffectiveTimeTo() {
        return this.effectiveTimeTo;
    }

    public SvcContract effectiveTimeTo(Instant effectiveTimeTo) {
        this.effectiveTimeTo = effectiveTimeTo;
        return this;
    }

    public void setEffectiveTimeTo(Instant effectiveTimeTo) {
        this.effectiveTimeTo = effectiveTimeTo;
    }

    public Integer getDurationMonth() {
        return this.durationMonth;
    }

    public SvcContract durationMonth(Integer durationMonth) {
        this.durationMonth = durationMonth;
        return this;
    }

    public void setDurationMonth(Integer durationMonth) {
        this.durationMonth = durationMonth;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public SvcContract value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getContractValue() {
        return this.contractValue;
    }

    public SvcContract contractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
        return this;
    }

    public void setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
    }

    public Integer getHumanResources() {
        return this.humanResources;
    }

    public SvcContract humanResources(Integer humanResources) {
        this.humanResources = humanResources;
        return this;
    }

    public void setHumanResources(Integer humanResources) {
        this.humanResources = humanResources;
    }

    public Integer getHumanResourcesWeekend() {
        return this.humanResourcesWeekend;
    }

    public SvcContract humanResourcesWeekend(Integer humanResourcesWeekend) {
        this.humanResourcesWeekend = humanResourcesWeekend;
        return this;
    }

    public void setHumanResourcesWeekend(Integer humanResourcesWeekend) {
        this.humanResourcesWeekend = humanResourcesWeekend;
    }

    public SvcContractStatus getStatus() {
        return this.status;
    }

    public SvcContract status(SvcContractStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SvcContractStatus status) {
        this.status = status;
    }

    public Long getSubjectCount() {
        return this.subjectCount;
    }

    public SvcContract subjectCount(Long subjectCount) {
        this.subjectCount = subjectCount;
        return this;
    }

    public void setSubjectCount(Long subjectCount) {
        this.subjectCount = subjectCount;
    }

    public BigDecimal getValuePerPerson() {
        return this.valuePerPerson;
    }

    public SvcContract valuePerPerson(BigDecimal valuePerPerson) {
        this.valuePerPerson = valuePerPerson;
        return this;
    }

    public void setValuePerPerson(BigDecimal valuePerPerson) {
        this.valuePerPerson = valuePerPerson;
    }

    public Integer getYear() {
        return this.year;
    }

    public SvcContract year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<SvcTarget> getTargets() {
        return this.targets;
    }

    public User getOwnerBy() {
        return this.ownerBy;
    }

    public SvcContract ownerBy(User user) {
        this.setOwnerBy(user);
        return this;
    }

    public void setOwnerBy(User user) {
        this.ownerBy = user;
    }

    public SvcUnit getUnit() {
        return this.unit;
    }

    public SvcContract unit(SvcUnit svcUnit) {
        this.setUnit(svcUnit);
        return this;
    }

    public void setUnit(SvcUnit svcUnit) {
        this.unit = svcUnit;
    }

    public OrgUser getSaler() {
        return this.saler;
    }

    public SvcContract saler(OrgUser orgUser) {
        this.setSaler(orgUser);
        return this;
    }

    public void setSaler(OrgUser orgUser) {
        this.saler = orgUser;
    }

    public SvcClient getClient() {
        return this.client;
    }

    public SvcContract client(SvcClient svcClient) {
        this.setClient(svcClient);
        return this;
    }

    public void setClient(SvcClient svcClient) {
        this.client = svcClient;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcContract)) {
            return false;
        }
        return id != null && id.equals(((SvcContract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcContract{" +
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
            "}";
    }

    public List<OrgGroup> getNotificationUnits() {
        return notificationUnits;
    }

    public void setNotificationUnits(List<OrgGroup> notificationUnits) {
        this.notificationUnits = notificationUnits;
    }

    public List<User> getManagerBy() {
        return managerBy;
    }

    public void setManagerBy(List<User> managerBy) {
        this.managerBy = managerBy;
    }

    public List<User> getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(List<User> approvedBy) {
        this.approvedBy = approvedBy;
    }
}
