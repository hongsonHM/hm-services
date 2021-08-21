package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUserRepository extends JpaRepository<OrgUser, Long>, JpaSpecificationExecutor<OrgUser> {
    Optional<OrgUser> findByInternalUser_Login(String username);

    Optional<OrgUser> findByInternalUser_Id(Long id);
}
