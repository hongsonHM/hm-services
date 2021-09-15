package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcGroupTask}.
 */
public interface SvcGroupTaskService {
    /**
     * Save a svcGroupTask.
     *
     * @param svcGroupTaskDTO the entity to save.
     * @return the persisted entity.
     */
    SvcGroupTaskDTO save(SvcGroupTaskDTO svcGroupTaskDTO);

    /**
     * Partially updates a svcGroupTask.
     *
     * @param svcGroupTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcGroupTaskDTO> partialUpdate(SvcGroupTaskDTO svcGroupTaskDTO);

    /**
     * Get all the svcGroupTasks.
     *
     * @return the list of entities.
     */
    List<SvcGroupTaskDTO> findAll();

    /**
     * Get the "id" svcGroupTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcGroupTaskDTO> findOne(Long id);

    /**
     * Delete the "id" svcGroupTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
