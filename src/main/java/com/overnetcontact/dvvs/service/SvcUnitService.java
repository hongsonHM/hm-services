package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcUnit}.
 */
public interface SvcUnitService {
    /**
     * Save a svcUnit.
     *
     * @param svcUnitDTO the entity to save.
     * @return the persisted entity.
     */
    SvcUnitDTO save(SvcUnitDTO svcUnitDTO);

    /**
     * Partially updates a svcUnit.
     *
     * @param svcUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcUnitDTO> partialUpdate(SvcUnitDTO svcUnitDTO);

    /**
     * Get all the svcUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcUnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcUnitDTO> findOne(Long id);

    /**
     * Delete the "id" svcUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
