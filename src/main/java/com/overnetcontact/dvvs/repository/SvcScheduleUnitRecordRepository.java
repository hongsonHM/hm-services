package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcScheduleUnitRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcScheduleUnitRecordRepository
    extends JpaRepository<SvcScheduleUnitRecord, Long>, JpaSpecificationExecutor<SvcScheduleUnitRecord> {}
