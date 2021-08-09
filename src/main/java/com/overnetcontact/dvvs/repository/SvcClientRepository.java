package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcClientRepository extends JpaRepository<SvcClient, Long>, JpaSpecificationExecutor<SvcClient> {
    Optional<SvcClient> findOneByCustomerNameIgnoreCaseAndAddressIgnoreCase(String customerName, String address);
}
