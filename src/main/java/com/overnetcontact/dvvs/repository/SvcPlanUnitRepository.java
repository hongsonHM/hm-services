package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcPlanUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcPlanUnitRepository extends JpaRepository<SvcPlanUnit, Long>, JpaSpecificationExecutor<SvcPlanUnit> {}
