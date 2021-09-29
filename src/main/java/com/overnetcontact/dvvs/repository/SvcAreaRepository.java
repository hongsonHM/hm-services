package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcAreaRepository extends JpaRepository<SvcArea, Long>, JpaSpecificationExecutor<SvcArea> {
    List<SvcArea> findByContractsId(Long id);
}
