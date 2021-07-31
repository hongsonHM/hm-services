package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.SvcClient}.
 */
public interface SvcClientService {
    /**
     * Save a svcClient.
     *
     * @param svcClientDTO the entity to save.
     * @return the persisted entity.
     */
    SvcClientDTO save(SvcClientDTO svcClientDTO);

    /**
     * Partially updates a svcClient.
     *
     * @param svcClientDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SvcClientDTO> partialUpdate(SvcClientDTO svcClientDTO);

    /**
     * Get all the svcClients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SvcClientDTO> findAll(Pageable pageable);

    /**
     * Get the "id" svcClient.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SvcClientDTO> findOne(Long id);

    /**
     * Delete the "id" svcClient.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
