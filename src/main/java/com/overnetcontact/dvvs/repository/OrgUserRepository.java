package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgUser entity.
 */
@Repository
public interface OrgUserRepository extends JpaRepository<OrgUser, Long> {
    Optional<OrgUser> findByInternalUser_Login(String username);
}
