package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcGroupRepository extends JpaRepository<SvcGroup, Long>, JpaSpecificationExecutor<SvcGroup> {
    Optional<SvcGroup> findOneByNameIgnoreCase(String name);
}
