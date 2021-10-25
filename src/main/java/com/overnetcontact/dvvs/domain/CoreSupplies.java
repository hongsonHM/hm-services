package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoreSupplies.
 */
@Entity
@Table(name = "core_supplies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoreSupplies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit")
    private String unit;

    @Column(name = "effort")
    private String effort;

    @Column(name = "category")
    private String category;

    @ManyToMany(mappedBy = "coreSupplies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "coreSupplies" }, allowSetters = true)
    private Set<CoreTask> coreTasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoreSupplies id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CoreSupplies name(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public CoreSupplies unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getEffort() {
        return this.effort;
    }

    public CoreSupplies effort(String effort) {
        this.effort = effort;
        return this;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public Set<CoreTask> getCoreTasks() {
        return this.coreTasks;
    }

    public CoreSupplies coreTasks(Set<CoreTask> coreTasks) {
        this.setCoreTasks(coreTasks);
        return this;
    }

    public CoreSupplies addCoreTask(CoreTask coreTask) {
        this.coreTasks.add(coreTask);
        coreTask.getCoreSupplies().add(this);
        return this;
    }

    public CoreSupplies removeCoreTask(CoreTask coreTask) {
        this.coreTasks.remove(coreTask);
        coreTask.getCoreSupplies().remove(this);
        return this;
    }

    public void setCoreTasks(Set<CoreTask> coreTasks) {
        if (this.coreTasks != null) {
            this.coreTasks.forEach(i -> i.removeCoreSupplies(this));
        }
        if (coreTasks != null) {
            coreTasks.forEach(i -> i.addCoreSupplies(this));
        }
        this.coreTasks = coreTasks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreSupplies)) {
            return false;
        }
        return id != null && id.equals(((CoreSupplies) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreSupplies{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", effort='" + getEffort() + "'" +
            "}";
    }
}
