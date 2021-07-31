package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcContract;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcContractRepository extends JpaRepository<SvcContract, Long> {}
