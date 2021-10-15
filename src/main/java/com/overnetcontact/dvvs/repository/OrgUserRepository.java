package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgGroup;
import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgUserRepository extends JpaRepository<OrgUser, Long>, JpaSpecificationExecutor<OrgUser> {
    Optional<OrgUser> findByInternalUser_Login(String username);

    Optional<OrgUser> findByInternalUser_Id(Long id);

    @Query("select distinct orgUser from OrgUser orgUser where orgUser.group in :groups")
    List<OrgUser> findByGroupIn(@Param("groups") List<OrgGroup> groups);

    @Query("select distinct orgUser from OrgUser orgUser where orgUser.group = :group")
    List<OrgUser> findByGroup(@Param("group") OrgGroup groups);

    @Query("select distinct orgUser from OrgUser orgUser where orgUser.internalUser in :ids")
    List<OrgUser> findByIdIn(@Param("ids") List<User> ids);
}
