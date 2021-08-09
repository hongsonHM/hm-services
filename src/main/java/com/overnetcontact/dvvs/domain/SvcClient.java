package com.overnetcontact.dvvs.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Client entity of SVC.\n@author KagariV.
 */
@Entity
@Table(name = "svc_client")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SvcClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 0, max = 200)
    @Column(name = "customer_name", length = 200, nullable = false)
    private String customerName;

    @NotNull
    @Column(name = "customer_city", nullable = false)
    private String customerCity;

    @NotNull
    @Size(min = 10, max = 11)
    @Column(name = "phone_number", length = 11, nullable = false)
    private String phoneNumber;

    @NotNull
    @Size(max = 300)
    @Column(name = "type", length = 300, nullable = false)
    private String type;

    @NotNull
    @Size(max = 300)
    @Column(name = "address", length = 300, nullable = false)
    private String address;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SvcClient id(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public SvcClient customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCity() {
        return this.customerCity;
    }

    public SvcClient customerCity(String customerCity) {
        this.customerCity = customerCity;
        return this;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public SvcClient phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return this.type;
    }

    public SvcClient type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return this.address;
    }

    public SvcClient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SvcClient)) {
            return false;
        }
        return id != null && id.equals(((SvcClient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SvcClient{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", customerCity='" + getCustomerCity() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", type='" + getType() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
