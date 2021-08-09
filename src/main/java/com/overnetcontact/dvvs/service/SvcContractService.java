package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcContract}.
 */
public interface SvcContractService {
    /**
     * Save a svcContract.
     *
     * @param svcContractDTO the entity to save.
     * @return the persisted entity.
     */
    SvcContractDTO save(SvcContractDTO svcContractDTO);

    /**
     * Partially updates a svcContract.
     *
     * @param svcContractDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcContractDTO> partialUpdate(SvcContractDTO svcContractDTO);

    /**
     * Get all the svcContracts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcContractDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcContract.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcContractDTO> findOne(Long id);

    /**
     * Delete the "id" svcContract.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Collection<SvcContractDTO> saveByExcel(MultipartFile file);
}
