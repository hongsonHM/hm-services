package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.OrgGroup;
import java.util.Optional;
import javax.validation.constraints.Size;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrgGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrgGroupRepository extends JpaRepository<OrgGroup, Long>, JpaSpecificationExecutor<OrgGroup> {
    Optional<OrgGroup> findByName(@Size(max = 100) String name);
}
