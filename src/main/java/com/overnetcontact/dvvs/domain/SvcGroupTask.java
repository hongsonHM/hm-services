package com.overnetcontact.dvvs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SvcGroupTask.
 */
@Entity
@Table(name = "svc_group_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcGroupTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "category")
    private String category;

    @JsonIgnoreProperties(value = { "svcGroupTask" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private SvcArea svcArea;

    @OneToMany(mappedBy = "svcGroupTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "svcGroupTask" }, allowSetters = true)
    private Set<SvcSpendTask> svcSpendTasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcGroupTask id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SvcGroupTask name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateDate() {
        return this.createDate;
    }

    public SvcGroupTask createDate(LocalDate createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public SvcGroupTask createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCategory() {
        return this.category;
    }

    public SvcGroupTask category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SvcArea getSvcArea() {
        return this.svcArea;
    }

    public SvcGroupTask svcArea(SvcArea svcArea) {
        this.setSvcArea(svcArea);
        return this;
    }

    public void setSvcArea(SvcArea svcArea) {
        this.svcArea = svcArea;
    }

    public Set<SvcSpendTask> getSvcSpendTasks() {
        return this.svcSpendTasks;
    }

    public SvcGroupTask svcSpendTasks(Set<SvcSpendTask> svcSpendTasks) {
        this.setSvcSpendTasks(svcSpendTasks);
        return this;
    }

    public SvcGroupTask addSvcSpendTask(SvcSpendTask svcSpendTask) {
        this.svcSpendTasks.add(svcSpendTask);
        svcSpendTask.setSvcGroupTask(this);
        return this;
    }

    public SvcGroupTask removeSvcSpendTask(SvcSpendTask svcSpendTask) {
        this.svcSpendTasks.remove(svcSpendTask);
        svcSpendTask.setSvcGroupTask(null);
        return this;
    }

    public void setSvcSpendTasks(Set<SvcSpendTask> svcSpendTasks) {
        if (this.svcSpendTasks != null) {
            this.svcSpendTasks.forEach(i -> i.setSvcGroupTask(null));
        }
        if (svcSpendTasks != null) {
            svcSpendTasks.forEach(i -> i.setSvcGroupTask(this));
        }
        this.svcSpendTasks = svcSpendTasks;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcGroupTask)) {
            return false;
        }
        return id != null && id.equals(((SvcGroupTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcGroupTask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
