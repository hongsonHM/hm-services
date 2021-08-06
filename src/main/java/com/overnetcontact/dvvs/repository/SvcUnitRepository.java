package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcUnitRepository extends JpaRepository<SvcUnit, Long> {}
