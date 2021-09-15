package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.CoreTask;
import java.util.List;
import java.util.Optional;
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
}
