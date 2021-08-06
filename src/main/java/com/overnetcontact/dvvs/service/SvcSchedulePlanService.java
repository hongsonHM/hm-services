package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcSchedulePlan}.
 */
public interface SvcSchedulePlanService {
    /**
     * Save a svcSchedulePlan.
     *
     * @param svcSchedulePlanDTO the entity to save.
     * @return the persisted entity.
     */
    SvcSchedulePlanDTO save(SvcSchedulePlanDTO svcSchedulePlanDTO);

    /**
     * Partially updates a svcSchedulePlan.
     *
     * @param svcSchedulePlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcSchedulePlanDTO> partialUpdate(SvcSchedulePlanDTO svcSchedulePlanDTO);

    /**
     * Get all the svcSchedulePlans.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcSchedulePlanDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcSchedulePlan.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcSchedulePlanDTO> findOne(Long id);

    /**
     * Delete the "id" svcSchedulePlan.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
