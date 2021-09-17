package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcPlanUnit}.
 */
public interface SvcPlanUnitService {
    /**
     * Save a svcPlanUnit.
     *
     * @param svcPlanUnitDTO the entity to save.
     * @return the persisted entity.
     */
    SvcPlanUnitDTO save(SvcPlanUnitDTO svcPlanUnitDTO);

    /**
     * Partially updates a svcPlanUnit.
     *
     * @param svcPlanUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcPlanUnitDTO> partialUpdate(SvcPlanUnitDTO svcPlanUnitDTO);

    /**
     * Get all the svcPlanUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcPlanUnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcPlanUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcPlanUnitDTO> findOne(Long id);

    /**
     * Delete the "id" svcPlanUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
