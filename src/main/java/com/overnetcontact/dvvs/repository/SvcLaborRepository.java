package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcLabor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcLabor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcLaborRepository extends JpaRepository<SvcLabor, Long>, JpaSpecificationExecutor<SvcLabor> {}
