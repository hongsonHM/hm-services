package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcSchedulePlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcSchedulePlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcSchedulePlanRepository extends JpaRepository<SvcSchedulePlan, Long> {}
