package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcSchedulePlanRecord entity.
 */
@Repository
public interface SvcSchedulePlanRecordRepository
    extends JpaRepository<SvcSchedulePlanRecord, Long>, JpaSpecificationExecutor<SvcSchedulePlanRecord> {
    @Query(
        value = "select distinct svcSchedulePlanRecord from SvcSchedulePlanRecord svcSchedulePlanRecord left join fetch svcSchedulePlanRecord.svcScheduleUnitRecords",
        countQuery = "select count(distinct svcSchedulePlanRecord) from SvcSchedulePlanRecord svcSchedulePlanRecord"
    )
    Page<SvcSchedulePlanRecord> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct svcSchedulePlanRecord from SvcSchedulePlanRecord svcSchedulePlanRecord left join fetch svcSchedulePlanRecord.svcScheduleUnitRecords"
    )
    List<SvcSchedulePlanRecord> findAllWithEagerRelationships();

    @Query(
        "select svcSchedulePlanRecord from SvcSchedulePlanRecord svcSchedulePlanRecord left join fetch svcSchedulePlanRecord.svcScheduleUnitRecords where svcSchedulePlanRecord.id =:id"
    )
    Optional<SvcSchedulePlanRecord> findOneWithEagerRelationships(@Param("id") Long id);
}
