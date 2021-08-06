package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcGroup}.
 */
public interface SvcGroupService {
    /**
     * Save a svcGroup.
     *
     * @param svcGroupDTO the entity to save.
     * @return the persisted entity.
     */
    SvcGroupDTO save(SvcGroupDTO svcGroupDTO);

    /**
     * Partially updates a svcGroup.
     *
     * @param svcGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcGroupDTO> partialUpdate(SvcGroupDTO svcGroupDTO);

    /**
     * Get all the svcGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcGroupDTO> findOne(Long id);

    /**
     * Delete the "id" svcGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
