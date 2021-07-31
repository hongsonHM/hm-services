package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcGroupRepository extends JpaRepository<SvcGroup, Long> {}
