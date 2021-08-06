package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcTarget;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcTarget entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcTargetRepository extends JpaRepository<SvcTarget, Long> {}
