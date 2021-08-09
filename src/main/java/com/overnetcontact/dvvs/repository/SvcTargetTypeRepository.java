package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcTargetType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcTargetType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcTargetTypeRepository extends JpaRepository<SvcTargetType, Long>, JpaSpecificationExecutor<SvcTargetType> {}
