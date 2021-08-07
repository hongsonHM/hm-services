package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcClient entity.
 */
@Repository
public interface SvcClientRepository extends JpaRepository<SvcClient, Long> {
    Optional<SvcClient> findOneByCustomerNameIgnoreCaseAndAddressIgnoreCase(String customerName, String address);
}
