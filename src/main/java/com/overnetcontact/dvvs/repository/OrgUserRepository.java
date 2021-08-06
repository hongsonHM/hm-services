package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUserRepository extends JpaRepository<OrgUser, Long> {}
