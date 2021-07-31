package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcTargetType}.
 */
public interface SvcTargetTypeService {
    /**
     * Save a svcTargetType.
     *
     * @param svcTargetTypeDTO the entity to save.
     * @return the persisted entity.
     */
    SvcTargetTypeDTO save(SvcTargetTypeDTO svcTargetTypeDTO);

    /**
     * Partially updates a svcTargetType.
     *
     * @param svcTargetTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcTargetTypeDTO> partialUpdate(SvcTargetTypeDTO svcTargetTypeDTO);

    /**
     * Get all the svcTargetTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcTargetTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcTargetType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcTargetTypeDTO> findOne(Long id);

    /**
     * Delete the "id" svcTargetType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
