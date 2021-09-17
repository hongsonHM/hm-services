package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcPlanTask}.
 */
public interface SvcPlanTaskService {
    /**
     * Save a svcPlanTask.
     *
     * @param svcPlanTaskDTO the entity to save.
     * @return the persisted entity.
     */
    SvcPlanTaskDTO save(SvcPlanTaskDTO svcPlanTaskDTO);

    /**
     * Partially updates a svcPlanTask.
     *
     * @param svcPlanTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcPlanTaskDTO> partialUpdate(SvcPlanTaskDTO svcPlanTaskDTO);

    /**
     * Get all the svcPlanTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcPlanTaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcPlanTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcPlanTaskDTO> findOne(Long id);

    /**
     * Delete the "id" svcPlanTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
