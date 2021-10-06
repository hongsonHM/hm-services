package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcPlanPart;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcPlanPart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcPlanPartRepository extends JpaRepository<SvcPlanPart, Long>, JpaSpecificationExecutor<SvcPlanPart> {}
