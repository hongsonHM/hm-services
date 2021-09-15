package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcSpendTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcSpendTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcSpendTaskRepository extends JpaRepository<SvcSpendTask, Long>, JpaSpecificationExecutor<SvcSpendTask> {}
