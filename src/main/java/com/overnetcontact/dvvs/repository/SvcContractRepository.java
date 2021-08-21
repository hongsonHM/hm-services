package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.domain.enumeration.SvcContractStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcContractRepository extends JpaRepository<SvcContract, Long>, JpaSpecificationExecutor<SvcContract> {
    Optional<SvcContract> findOneByDocumentIdIgnoreCase(String lowerCase);

    @Query(
        "select svcContract from SvcContract svcContract where svcContract.status = :whichType " +
        "and svcContract.effectiveTimeTo <= :timeItEnd"
    )
    List<SvcContract> findWhatWillEnd(@Param("timeItEnd") Instant timeItEnd, @Param("whichType") SvcContractStatus whichType);
}
