package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcContract;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcContract entity.
 */
@Repository
public interface SvcContractRepository extends JpaRepository<SvcContract, Long> {
    Optional<SvcContract> findOneByDocumentIdIgnoreCase(String lowerCase);
}
