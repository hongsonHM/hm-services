package com.overnetcontact.dvvs.repository;

import com.overnetcontact.dvvs.domain.SvcContract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SvcContract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SvcContractRepository extends JpaRepository<SvcContract, Long>, JpaSpecificationExecutor<SvcContract> {
    @Query("select svcContract from SvcContract svcContract where svcContract.approvedBy.login = ?#{principal.username}")
    List<SvcContract> findByApprovedByIsCurrentUser();

    @Query("select svcContract from SvcContract svcContract where svcContract.ownerBy.login = ?#{principal.username}")
    List<SvcContract> findByOwnerByIsCurrentUser();

    Optional<SvcContract> findOneByDocumentIdIgnoreCase(String lowerCase);
}
