package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
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

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @NotNull
    @Column(name = "effective_date", nullable = false)
    private Instant effectiveDate;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private Instant expirationDate;

    @OneToMany(mappedBy = "svcContract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "childs", "type", "svcTarget", "svcContract" }, allowSetters = true)
    private Set<SvcTarget> targets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "group" }, allowSetters = true)
    private SvcUnit unit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "internalUser", "group" }, allowSetters = true)
    private OrgUser saler;

    @ManyToOne
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

    public String getName() {
        return this.name;
    }

    public SvcContract name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getEffectiveDate() {
        return this.effectiveDate;
    }

    public SvcContract effectiveDate(Instant effectiveDate) {
        this.effectiveDate = effectiveDate;
        return this;
    }

    public void setEffectiveDate(Instant effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Instant getExpirationDate() {
        return this.expirationDate;
    }

    public SvcContract expirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<SvcTarget> getTargets() {
        return this.targets;
    }

    public SvcContract targets(Set<SvcTarget> svcTargets) {
        this.setTargets(svcTargets);
        return this;
    }

    public SvcContract addTargets(SvcTarget svcTarget) {
        this.targets.add(svcTarget);
        svcTarget.setSvcContract(this);
        return this;
    }

    public SvcContract removeTargets(SvcTarget svcTarget) {
        this.targets.remove(svcTarget);
        svcTarget.setSvcContract(null);
        return this;
    }

    public void setTargets(Set<SvcTarget> svcTargets) {
        if (this.targets != null) {
            this.targets.forEach(i -> i.setSvcContract(null));
        }
        if (svcTargets != null) {
            svcTargets.forEach(i -> i.setSvcContract(this));
        }
        this.targets = svcTargets;
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
            ", name='" + getName() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            "}";
    }
}
