package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcPlan;
import liquibase.pro.packaged.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcPlanRepository extends JpaRepository<SvcPlan, Long>, JpaSpecificationExecutor<SvcPlan> {
    //    Page<SvcPlan> findAll(@Nullable Specification<SvcPlan> var1, Pageable var2);

}
