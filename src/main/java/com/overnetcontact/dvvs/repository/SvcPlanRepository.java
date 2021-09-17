package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcPlanRepository extends JpaRepository<SvcPlan, Long>, JpaSpecificationExecutor<SvcPlan> {}
