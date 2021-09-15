package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.CoreSupplies;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CoreSupplies entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CoreSuppliesRepository extends JpaRepository<CoreSupplies, Long>, JpaSpecificationExecutor<CoreSupplies> {}
