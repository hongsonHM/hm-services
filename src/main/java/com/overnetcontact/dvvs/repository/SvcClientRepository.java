package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcClientRepository extends JpaRepository<SvcClient, Long> {}
