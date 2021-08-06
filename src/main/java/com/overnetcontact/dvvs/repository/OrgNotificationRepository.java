package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgNotification;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgNotificationRepository extends JpaRepository<OrgNotification, Long> {}
