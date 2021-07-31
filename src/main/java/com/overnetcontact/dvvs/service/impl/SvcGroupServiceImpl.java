package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcGroup;
import com.overnetcontact.dvvs.repository.SvcGroupRepository;
import com.overnetcontact.dvvs.service.SvcGroupService;
import com.overnetcontact.dvvs.service.dto.SvcGroupDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcGroup}.
 */
@Service
@Transactional
public class SvcGroupServiceImpl implements SvcGroupService {

    private final Logger log = LoggerFactory.getLogger(SvcGroupServiceImpl.class);

    private final SvcGroupRepository svcGroupRepository;

    private final SvcGroupMapper svcGroupMapper;

    public SvcGroupServiceImpl(SvcGroupRepository svcGroupRepository, SvcGroupMapper svcGroupMapper) {
        this.svcGroupRepository = svcGroupRepository;
        this.svcGroupMapper = svcGroupMapper;
    }

    @Override
    public SvcGroupDTO save(SvcGroupDTO svcGroupDTO) {
        log.debug("Request to save SvcGroup : {}", svcGroupDTO);
        SvcGroup svcGroup = svcGroupMapper.toEntity(svcGroupDTO);
        svcGroup = svcGroupRepository.save(svcGroup);
        return svcGroupMapper.toDto(svcGroup);
    }

    @Override
    public Optional<SvcGroupDTO> partialUpdate(SvcGroupDTO svcGroupDTO) {
        log.debug("Request to partially update SvcGroup : {}", svcGroupDTO);

        return svcGroupRepository
            .findById(svcGroupDTO.getId())
            .map(
                existingSvcGroup -> {
                    svcGroupMapper.partialUpdate(existingSvcGroup, svcGroupDTO);

                    return existingSvcGroup;
                }
            )
            .map(svcGroupRepository::save)
            .map(svcGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcGroups");
        return svcGroupRepository.findAll(pageable).map(svcGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcGroupDTO> findOne(Long id) {
        log.debug("Request to get SvcGroup : {}", id);
        return svcGroupRepository.findById(id).map(svcGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcGroup : {}", id);
        svcGroupRepository.deleteById(id);
    }
}
