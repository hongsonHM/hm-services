package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CoreTask.
 */
@Entity
@Table(name = "core_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CoreTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit")
    private String unit;

    @Column(name = "category")
    private String category;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_core_task__core_supplies",
        joinColumns = @JoinColumn(name = "core_task_id"),
        inverseJoinColumns = @JoinColumn(name = "core_supplies_id")
    )
    @JsonIgnoreProperties(value = { "coreTasks" }, allowSetters = true)
    private Set<CoreSupplies> coreSupplies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CoreTask id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CoreTask name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public CoreTask unit(String unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return this.category;
    }

    public CoreTask category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return this.note;
    }

    public CoreTask note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<CoreSupplies> getCoreSupplies() {
        return this.coreSupplies;
    }

    public CoreTask coreSupplies(Set<CoreSupplies> coreSupplies) {
        this.setCoreSupplies(coreSupplies);
        return this;
    }

    public CoreTask addCoreSupplies(CoreSupplies coreSupplies) {
        this.coreSupplies.add(coreSupplies);
        coreSupplies.getCoreTasks().add(this);
        return this;
    }

    public CoreTask removeCoreSupplies(CoreSupplies coreSupplies) {
        this.coreSupplies.remove(coreSupplies);
        coreSupplies.getCoreTasks().remove(this);
        return this;
    }

    public void setCoreSupplies(Set<CoreSupplies> coreSupplies) {
        this.coreSupplies = coreSupplies;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CoreTask)) {
            return false;
        }
        return id != null && id.equals(((CoreTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CoreTask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit='" + getUnit() + "'" +
            ", category='" + getCategory() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
