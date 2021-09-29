package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.CoreTask;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CoreTask entity.
 */
@Repository
public interface CoreTaskRepository extends JpaRepository<CoreTask, Long>, JpaSpecificationExecutor<CoreTask> {
    @Query(
        value = "select distinct coreTask from CoreTask coreTask left join fetch coreTask.coreSupplies",
        countQuery = "select count(distinct coreTask) from CoreTask coreTask"
    )
    Page<CoreTask> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct coreTask from CoreTask coreTask left join fetch coreTask.coreSupplies")
    List<CoreTask> findAllWithEagerRelationships();

    @Query("select coreTask from CoreTask coreTask left join fetch coreTask.coreSupplies where coreTask.id =:id")
    Optional<CoreTask> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select distinct coreTask from CoreTask coreTask left join fetch coreTask.coreSupplies where coreTask.id in :ids")
    List<CoreTask> findByIdIn(@Param("ids") Set<Long> ids);

    @Query(
        value = "SELECT distinct cs.name, cs.unit, cs.effort from core_task as ct " +
        "inner join rel_core_task__core_supplies as rctcs on ct.id = rctcs.core_task_id " +
        "inner join core_supplies as cs on rctcs.core_supplies_id = cs.id " +
        "where ct.id in :ids",
        nativeQuery = true
    )
    List<Object> findSuppliesWithTask(@Param("ids") List<Long> ids);
}
