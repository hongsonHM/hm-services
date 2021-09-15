package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcGroupTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcGroupTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcGroupTaskRepository extends JpaRepository<SvcGroupTask, Long>, JpaSpecificationExecutor<SvcGroupTask> {}
