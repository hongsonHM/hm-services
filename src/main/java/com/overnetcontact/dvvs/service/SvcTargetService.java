package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcTarget}.
 */
public interface SvcTargetService {
    /**
     * Save a svcTarget.
     *
     * @param svcTargetDTO the entity to save.
     * @return the persisted entity.
     */
    SvcTargetDTO save(SvcTargetDTO svcTargetDTO);

    /**
     * Partially updates a svcTarget.
     *
     * @param svcTargetDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcTargetDTO> partialUpdate(SvcTargetDTO svcTargetDTO);

    /**
     * Get all the svcTargets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcTargetDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcTarget.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcTargetDTO> findOne(Long id);

    /**
     * Delete the "id" svcTarget.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
