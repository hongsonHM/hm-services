package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.domain.SvcSpendTask;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcSpendTask}.
 */
public interface SvcSpendTaskService {
    /**
     * Save a svcSpendTask.
     *
     * @param svcSpendTaskDTO the entity to save.
     * @return the persisted entity.
     */
    SvcSpendTaskDTO save(SvcSpendTaskDTO svcSpendTaskDTO);

    /**
     * Partially updates a svcSpendTask.
     *
     * @param svcSpendTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcSpendTaskDTO> partialUpdate(SvcSpendTaskDTO svcSpendTaskDTO);

    /**
     * Get all the svcSpendTasks.
     *
     * @return the list of entities.
     */
    List<SvcSpendTaskDTO> findAll();

    /**
     * Get the "id" svcSpendTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcSpendTaskDTO> findOne(Long id);

    /**
     * Delete the "id" svcSpendTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Object> findIds(List<Long> ids);

    Set<SvcSpendTaskDTO> findBySvcGroupTask(SvcGroupTask svcGroupTask);
}
