package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord}.
 */
public interface SvcSchedulePlanRecordService {
    /**
     * Save a svcSchedulePlanRecord.
     *
     * @param svcSchedulePlanRecordDTO the entity to save.
     * @return the persisted entity.
     */
    SvcSchedulePlanRecordDTO save(SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO);

    /**
     * Partially updates a svcSchedulePlanRecord.
     *
     * @param svcSchedulePlanRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcSchedulePlanRecordDTO> partialUpdate(SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO);

    /**
     * Get all the svcSchedulePlanRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcSchedulePlanRecordDTO> findAll(Pageable pageable);

    /**
     * Get all the svcSchedulePlanRecords with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcSchedulePlanRecordDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" svcSchedulePlanRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcSchedulePlanRecordDTO> findOne(Long id);

    /**
     * Delete the "id" svcSchedulePlanRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
