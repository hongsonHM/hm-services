package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Target(which will be work on) entity of SVC.\n@author KagariV.
 */
@Entity
@Table(name = "svc_target")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @OneToMany(mappedBy = "svcTarget")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "childs", "type", "svcTarget", "svcContract" }, allowSetters = true)
    private Set<SvcTarget> childs = new HashSet<>();

    @ManyToOne
    private SvcTargetType type;

    @ManyToOne
    @JsonIgnoreProperties(value = { "childs", "type", "svcTarget", "svcContract" }, allowSetters = true)
    private SvcTarget svcTarget;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcTarget id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SvcTarget name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SvcTarget> getChilds() {
        return this.childs;
    }

    public SvcTarget childs(Set<SvcTarget> svcTargets) {
        this.setChilds(svcTargets);
        return this;
    }

    public SvcTarget addChilds(SvcTarget svcTarget) {
        this.childs.add(svcTarget);
        svcTarget.setSvcTarget(this);
        return this;
    }

    public SvcTarget removeChilds(SvcTarget svcTarget) {
        this.childs.remove(svcTarget);
        svcTarget.setSvcTarget(null);
        return this;
    }

    public void setChilds(Set<SvcTarget> svcTargets) {
        if (this.childs != null) {
            this.childs.forEach(i -> i.setSvcTarget(null));
        }
        if (svcTargets != null) {
            svcTargets.forEach(i -> i.setSvcTarget(this));
        }
        this.childs = svcTargets;
    }

    public SvcTargetType getType() {
        return this.type;
    }

    public SvcTarget type(SvcTargetType svcTargetType) {
        this.setType(svcTargetType);
        return this;
    }

    public void setType(SvcTargetType svcTargetType) {
        this.type = svcTargetType;
    }

    public SvcTarget getSvcTarget() {
        return this.svcTarget;
    }

    public SvcTarget svcTarget(SvcTarget svcTarget) {
        this.setSvcTarget(svcTarget);
        return this;
    }

    public void setSvcTarget(SvcTarget svcTarget) {
        this.svcTarget = svcTarget;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcTarget)) {
            return false;
        }
        return id != null && id.equals(((SvcTarget) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcTarget{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
