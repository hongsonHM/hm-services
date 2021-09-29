package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcArea.
 */
@Entity
@Table(name = "svc_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "type")
    private String type;

    @Column(name = "contracts_id")
    private Long contractsId;

    @JsonIgnoreProperties(value = { "svcArea", "svcSpendTasks" }, allowSetters = true)
    @OneToOne(mappedBy = "svcArea")
    private SvcGroupTask svcGroupTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcArea id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SvcArea name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return this.location;
    }

    public SvcArea location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return this.type;
    }

    public SvcArea type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getContractsId() {
        return this.contractsId;
    }

    public SvcArea contractsId(Long contractsId) {
        this.contractsId = contractsId;
        return this;
    }

    public void setContractsId(Long contractsId) {
        this.contractsId = contractsId;
    }

    public SvcGroupTask getSvcGroupTask() {
        return this.svcGroupTask;
    }

    public SvcArea svcGroupTask(SvcGroupTask svcGroupTask) {
        this.setSvcGroupTask(svcGroupTask);
        return this;
    }

    public void setSvcGroupTask(SvcGroupTask svcGroupTask) {
        if (this.svcGroupTask != null) {
            this.svcGroupTask.setSvcArea(null);
        }
        if (svcGroupTask != null) {
            svcGroupTask.setSvcArea(this);
        }
        this.svcGroupTask = svcGroupTask;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcArea)) {
            return false;
        }
        return id != null && id.equals(((SvcArea) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcArea{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            ", type='" + getType() + "'" +
            ", contractsId=" + getContractsId() +
            "}";
    }
}
