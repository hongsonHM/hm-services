package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.OrgNotification;
import com.overnetcontact.dvvs.repository.OrgNotificationRepository;
import com.overnetcontact.dvvs.service.OrgNotificationService;
import com.overnetcontact.dvvs.service.dto.OrgNotificationDTO;
import com.overnetcontact.dvvs.service.mapper.OrgNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgNotification}.
 */
@Service
@Transactional
public class OrgNotificationServiceImpl implements OrgNotificationService {

    private final Logger log = LoggerFactory.getLogger(OrgNotificationServiceImpl.class);

    private final OrgNotificationRepository orgNotificationRepository;

    private final OrgNotificationMapper orgNotificationMapper;

    public OrgNotificationServiceImpl(OrgNotificationRepository orgNotificationRepository, OrgNotificationMapper orgNotificationMapper) {
        this.orgNotificationRepository = orgNotificationRepository;
        this.orgNotificationMapper = orgNotificationMapper;
    }

    @Override
    public OrgNotificationDTO save(OrgNotificationDTO orgNotificationDTO) {
        log.debug("Request to save OrgNotification : {}", orgNotificationDTO);
        OrgNotification orgNotification = orgNotificationMapper.toEntity(orgNotificationDTO);
        orgNotification = orgNotificationRepository.save(orgNotification);
        return orgNotificationMapper.toDto(orgNotification);
    }

    @Override
    public Optional<OrgNotificationDTO> partialUpdate(OrgNotificationDTO orgNotificationDTO) {
        log.debug("Request to partially update OrgNotification : {}", orgNotificationDTO);

        return orgNotificationRepository
            .findById(orgNotificationDTO.getId())
            .map(
                existingOrgNotification -> {
                    orgNotificationMapper.partialUpdate(existingOrgNotification, orgNotificationDTO);

                    return existingOrgNotification;
                }
            )
            .map(orgNotificationRepository::save)
            .map(orgNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrgNotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrgNotifications");
        return orgNotificationRepository.findAll(pageable).map(orgNotificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrgNotificationDTO> findOne(Long id) {
        log.debug("Request to get OrgNotification : {}", id);
        return orgNotificationRepository.findById(id).map(orgNotificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrgNotification : {}", id);
        orgNotificationRepository.deleteById(id);
    }
}
