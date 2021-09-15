package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.CoreTask}.
 */
public interface CoreTaskService {
    /**
     * Save a coreTask.
     *
     * @param coreTaskDTO the entity to save.
     * @return the persisted entity.
     */
    CoreTaskDTO save(CoreTaskDTO coreTaskDTO);

    /**
     * Partially updates a coreTask.
     *
     * @param coreTaskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoreTaskDTO> partialUpdate(CoreTaskDTO coreTaskDTO);

    /**
     * Get all the coreTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoreTaskDTO> findAll(Pageable pageable);

    /**
     * Get all the coreTasks with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoreTaskDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" coreTask.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoreTaskDTO> findOne(Long id);

    /**
     * Delete the "id" coreTask.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
