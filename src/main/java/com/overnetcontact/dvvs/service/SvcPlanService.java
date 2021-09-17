package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcPlan}.
 */
public interface SvcPlanService {
    /**
     * Save a svcPlan.
     *
     * @param svcPlanDTO the entity to save.
     * @return the persisted entity.
     */
    SvcPlanDTO save(SvcPlanDTO svcPlanDTO);

    /**
     * Partially updates a svcPlan.
     *
     * @param svcPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcPlanDTO> partialUpdate(SvcPlanDTO svcPlanDTO);

    /**
     * Get all the svcPlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcPlanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcPlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcPlanDTO> findOne(Long id);

    /**
     * Delete the "id" svcPlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
