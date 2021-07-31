package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcScheduleUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcScheduleUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcScheduleUnitRepository extends JpaRepository<SvcScheduleUnit, Long> {}
