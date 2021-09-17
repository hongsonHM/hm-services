package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcLabor.
 */
@Entity
@Table(name = "svc_labor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcLabor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "labor_code")
    private String laborCode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "svcLabors", "svcPlanTasks", "svcPlan" }, allowSetters = true)
    private SvcPlanUnit svcPlanUnit;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcLabor id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SvcLabor name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public SvcLabor phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public SvcLabor address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLaborCode() {
        return this.laborCode;
    }

    public SvcLabor laborCode(String laborCode) {
        this.laborCode = laborCode;
        return this;
    }

    public void setLaborCode(String laborCode) {
        this.laborCode = laborCode;
    }

    public SvcPlanUnit getSvcPlanUnit() {
        return this.svcPlanUnit;
    }

    public SvcLabor svcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.setSvcPlanUnit(svcPlanUnit);
        return this;
    }

    public void setSvcPlanUnit(SvcPlanUnit svcPlanUnit) {
        this.svcPlanUnit = svcPlanUnit;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcLabor)) {
            return false;
        }
        return id != null && id.equals(((SvcLabor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcLabor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", laborCode='" + getLaborCode() + "'" +
            "}";
    }
}
