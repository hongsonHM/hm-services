package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.SvcPlanPart;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcPlanPart}.
 */
public interface SvcPlanPartService {
    /**
     * Save a svcPlanPart.
     *
     * @param svcPlanPartDTO the entity to save.
     * @return the persisted entity.
     */
    SvcPlanPartDTO save(SvcPlanPartDTO svcPlanPartDTO);

    /**
     * Partially updates a svcPlanPart.
     *
     * @param svcPlanPartDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcPlanPartDTO> partialUpdate(SvcPlanPartDTO svcPlanPartDTO);

    /**
     * Get all the svcPlanParts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcPlanPartDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcPlanPart.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcPlanPartDTO> findOne(Long id);

    /**
     * Delete the "id" svcPlanPart.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<SvcPlanPartDTO> findByPlanUnitID(Long planUnitID);
}
