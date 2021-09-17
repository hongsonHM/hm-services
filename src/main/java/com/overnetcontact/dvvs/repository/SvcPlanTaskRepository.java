package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcPlanTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcPlanTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcPlanTaskRepository extends JpaRepository<SvcPlanTask, Long>, JpaSpecificationExecutor<SvcPlanTask> {}
