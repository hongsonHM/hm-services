package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord}.
 */
public interface SvcScheduleUnitRecordService {
    /**
     * Save a svcScheduleUnitRecord.
     *
     * @param svcScheduleUnitRecordDTO the entity to save.
     * @return the persisted entity.
     */
    SvcScheduleUnitRecordDTO save(SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO);

    /**
     * Partially updates a svcScheduleUnitRecord.
     *
     * @param svcScheduleUnitRecordDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcScheduleUnitRecordDTO> partialUpdate(SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO);

    /**
     * Get all the svcScheduleUnitRecords.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcScheduleUnitRecordDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcScheduleUnitRecord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcScheduleUnitRecordDTO> findOne(Long id);

    /**
     * Delete the "id" svcScheduleUnitRecord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
