package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcGroup entity.
 */
@Repository
public interface SvcGroupRepository extends JpaRepository<SvcGroup, Long> {
    Optional<SvcGroup> findOneByNameIgnoreCase(String name);
}
