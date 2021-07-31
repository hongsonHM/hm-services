package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcScheduleUnit}.
 */
public interface SvcScheduleUnitService {
    /**
     * Save a svcScheduleUnit.
     *
     * @param svcScheduleUnitDTO the entity to save.
     * @return the persisted entity.
     */
    SvcScheduleUnitDTO save(SvcScheduleUnitDTO svcScheduleUnitDTO);

    /**
     * Partially updates a svcScheduleUnit.
     *
     * @param svcScheduleUnitDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcScheduleUnitDTO> partialUpdate(SvcScheduleUnitDTO svcScheduleUnitDTO);

    /**
     * Get all the svcScheduleUnits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcScheduleUnitDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcScheduleUnit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcScheduleUnitDTO> findOne(Long id);

    /**
     * Delete the "id" svcScheduleUnit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
