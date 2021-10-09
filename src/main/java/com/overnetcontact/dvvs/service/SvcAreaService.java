package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcArea}.
 */
public interface SvcAreaService {
    /**
     * Save a svcArea.
     *
     * @param svcAreaDTO the entity to save.
     * @return the persisted entity.
     */
    SvcAreaDTO save(SvcAreaDTO svcAreaDTO);

    /**
     * Partially updates a svcArea.
     *
     * @param svcAreaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcAreaDTO> partialUpdate(SvcAreaDTO svcAreaDTO);

    /**
     * Get all the svcAreas.
     *
     * @return the list of entities.
     */
    List<SvcAreaDTO> findAll();
    /**
     * Get all the SvcAreaDTO where SvcGroupTask is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<SvcAreaDTO> findAllWhereSvcGroupTaskIsNull();

    /**
     * Get the "id" svcArea.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcAreaDTO> findOne(Long id);

    /**
     * Delete the "id" svcArea.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SvcArea> findByContractsId(Long id);
}
