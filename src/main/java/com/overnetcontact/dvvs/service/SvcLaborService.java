package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcLabor}.
 */
public interface SvcLaborService {
    /**
     * Save a svcLabor.
     *
     * @param svcLaborDTO the entity to save.
     * @return the persisted entity.
     */
    SvcLaborDTO save(SvcLaborDTO svcLaborDTO);

    /**
     * Partially updates a svcLabor.
     *
     * @param svcLaborDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcLaborDTO> partialUpdate(SvcLaborDTO svcLaborDTO);

    /**
     * Get all the svcLabors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcLaborDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcLabor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcLaborDTO> findOne(Long id);

    /**
     * Delete the "id" svcLabor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
