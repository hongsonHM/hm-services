package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcGroupTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcGroupTaskRepository extends JpaRepository<SvcGroupTask, Long>, JpaSpecificationExecutor<SvcGroupTask> {
    Optional<SvcGroupTask> findBySvcArea(SvcArea svcArea);
}
