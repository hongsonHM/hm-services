package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcArea;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcAreaRepository extends JpaRepository<SvcArea, Long>, JpaSpecificationExecutor<SvcArea> {}
