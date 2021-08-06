package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.OrgGroup;
import com.overnetcontact.dvvs.repository.OrgGroupRepository;
import com.overnetcontact.dvvs.service.dto.OrgGroupDTO;
import com.overnetcontact.dvvs.service.mapper.OrgGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrgGroup}.
 */
@Service
@Transactional
public class OrgGroupService {

    private final Logger log = LoggerFactory.getLogger(OrgGroupService.class);

    private final OrgGroupRepository orgGroupRepository;

    private final OrgGroupMapper orgGroupMapper;

    public OrgGroupService(OrgGroupRepository orgGroupRepository, OrgGroupMapper orgGroupMapper) {
        this.orgGroupRepository = orgGroupRepository;
        this.orgGroupMapper = orgGroupMapper;
    }

    /**
     * Save a orgGroup.
     *
     * @param orgGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public OrgGroupDTO save(OrgGroupDTO orgGroupDTO) {
        log.debug("Request to save OrgGroup : {}", orgGroupDTO);
        OrgGroup orgGroup = orgGroupMapper.toEntity(orgGroupDTO);
        orgGroup = orgGroupRepository.save(orgGroup);
        return orgGroupMapper.toDto(orgGroup);
    }

    /**
     * Partially update a orgGroup.
     *
     * @param orgGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrgGroupDTO> partialUpdate(OrgGroupDTO orgGroupDTO) {
        log.debug("Request to partially update OrgGroup : {}", orgGroupDTO);

        return orgGroupRepository
            .findById(orgGroupDTO.getId())
            .map(
                existingOrgGroup -> {
                    orgGroupMapper.partialUpdate(existingOrgGroup, orgGroupDTO);

                    return existingOrgGroup;
                }
            )
            .map(orgGroupRepository::save)
            .map(orgGroupMapper::toDto);
    }

    /**
     * Get all the orgGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrgGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrgGroups");
        return orgGroupRepository.findAll(pageable).map(orgGroupMapper::toDto);
    }

    /**
     * Get one orgGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrgGroupDTO> findOne(Long id) {
        log.debug("Request to get OrgGroup : {}", id);
        return orgGroupRepository.findById(id).map(orgGroupMapper::toDto);
    }

    /**
     * Delete the orgGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrgGroup : {}", id);
        orgGroupRepository.deleteById(id);
    }
}
