package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.overnetcontact.dvvs.domain.OrgNotification}.
 */
public interface OrgNotificationService {
    /**
     * Save a orgNotification.
     *
     * @param orgNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    OrgNotificationDTO save(OrgNotificationDTO orgNotificationDTO);

    /**
     * Partially updates a orgNotification.
     *
     * @param orgNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrgNotificationDTO> partialUpdate(OrgNotificationDTO orgNotificationDTO);

    /**
     * Get all the orgNotifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrgNotificationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" orgNotification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrgNotificationDTO> findOne(Long id);

    /**
     * Delete the "id" orgNotification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
