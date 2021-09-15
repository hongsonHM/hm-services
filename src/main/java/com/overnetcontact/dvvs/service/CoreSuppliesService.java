package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.CoreSupplies}.
 */
public interface CoreSuppliesService {
    /**
     * Save a coreSupplies.
     *
     * @param coreSuppliesDTO the entity to save.
     * @return the persisted entity.
     */
    CoreSuppliesDTO save(CoreSuppliesDTO coreSuppliesDTO);

    /**
     * Partially updates a coreSupplies.
     *
     * @param coreSuppliesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CoreSuppliesDTO> partialUpdate(CoreSuppliesDTO coreSuppliesDTO);

    /**
     * Get all the coreSupplies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CoreSuppliesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" coreSupplies.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CoreSuppliesDTO> findOne(Long id);

    /**
     * Delete the "id" coreSupplies.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
