package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.OrgUser;
import com.overnetcontact.dvvs.repository.OrgUserRepository;
import com.overnetcontact.dvvs.service.dto.OrgUserDTO;
import com.overnetcontact.dvvs.service.mapper.OrgUserMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgUser}.
 */
@Service
@Transactional
public class OrgUserService {

    private final Logger log = LoggerFactory.getLogger(OrgUserService.class);

    private final OrgUserRepository orgUserRepository;

    private final OrgUserMapper orgUserMapper;

    public OrgUserService(OrgUserRepository orgUserRepository, OrgUserMapper orgUserMapper) {
        this.orgUserRepository = orgUserRepository;
        this.orgUserMapper = orgUserMapper;
    }

    /**
     * Save a orgUser.
     *
     * @param orgUserDTO the entity to save.
     * @return the persisted entity.
     */
    public OrgUserDTO save(OrgUserDTO orgUserDTO) {
        log.debug("Request to save OrgUser : {}", orgUserDTO);
        OrgUser orgUser = orgUserMapper.toEntity(orgUserDTO);
        orgUser = orgUserRepository.save(orgUser);
        return orgUserMapper.toDto(orgUser);
    }

    /**
     * Partially update a orgUser.
     *
     * @param orgUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrgUserDTO> partialUpdate(OrgUserDTO orgUserDTO) {
        log.debug("Request to partially update OrgUser : {}", orgUserDTO);

        return orgUserRepository
            .findById(orgUserDTO.getId())
            .map(
                existingOrgUser -> {
                    orgUserMapper.partialUpdate(existingOrgUser, orgUserDTO);

                    return existingOrgUser;
                }
            )
            .map(orgUserRepository::save)
            .map(orgUserMapper::toDto);
    }

    /**
     * Get all the orgUsers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrgUsers");
        return orgUserRepository.findAll(pageable).map(orgUserMapper::toDto);
    }

    /**
     * Get one orgUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrgUserDTO> findOne(Long id) {
        log.debug("Request to get OrgUser : {}", id);
        return orgUserRepository.findById(id).map(orgUserMapper::toDto);
    }

    /**
     * Delete the orgUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgUser : {}", id);
        orgUserRepository.deleteById(id);
    }
}
