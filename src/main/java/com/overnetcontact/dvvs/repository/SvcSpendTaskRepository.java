package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.domain.SvcSpendTask;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcSpendTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcSpendTaskRepository extends JpaRepository<SvcSpendTask, Long>, JpaSpecificationExecutor<SvcSpendTask> {
    @Query(
        value = "SELECT distinct cs.name, cs.unit, cs.effort from svc_spend_task as sst " +
        "inner join rel_core_task__core_supplies as rctcs on sst.core_task_id = rctcs.core_task_id " +
        "inner join core_supplies as cs on rctcs.core_supplies_id = cs.id " +
        "where sst.core_task_id in :ids",
        nativeQuery = true
    )
    List<Object> findByIds(@Param("ids") List<Long> ids);

    Set<SvcSpendTask> findBySvcGroupTask(SvcGroupTask svcGroupTask);
}
