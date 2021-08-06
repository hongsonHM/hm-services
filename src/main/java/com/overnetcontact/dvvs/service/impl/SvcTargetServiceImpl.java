package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcTarget;
import com.overnetcontact.dvvs.repository.SvcTargetRepository;
import com.overnetcontact.dvvs.service.SvcTargetService;
import com.overnetcontact.dvvs.service.dto.SvcTargetDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcTarget}.
 */
@Service
@Transactional
public class SvcTargetServiceImpl implements SvcTargetService {

    private final Logger log = LoggerFactory.getLogger(SvcTargetServiceImpl.class);

    private final SvcTargetRepository svcTargetRepository;

    private final SvcTargetMapper svcTargetMapper;

    public SvcTargetServiceImpl(SvcTargetRepository svcTargetRepository, SvcTargetMapper svcTargetMapper) {
        this.svcTargetRepository = svcTargetRepository;
        this.svcTargetMapper = svcTargetMapper;
    }

    @Override
    public SvcTargetDTO save(SvcTargetDTO svcTargetDTO) {
        log.debug("Request to save SvcTarget : {}", svcTargetDTO);
        SvcTarget svcTarget = svcTargetMapper.toEntity(svcTargetDTO);
        svcTarget = svcTargetRepository.save(svcTarget);
        return svcTargetMapper.toDto(svcTarget);
    }

    @Override
    public Optional<SvcTargetDTO> partialUpdate(SvcTargetDTO svcTargetDTO) {
        log.debug("Request to partially update SvcTarget : {}", svcTargetDTO);

        return svcTargetRepository
            .findById(svcTargetDTO.getId())
            .map(
                existingSvcTarget -> {
                    svcTargetMapper.partialUpdate(existingSvcTarget, svcTargetDTO);

                    return existingSvcTarget;
                }
            )
            .map(svcTargetRepository::save)
            .map(svcTargetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcTargetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcTargets");
        return svcTargetRepository.findAll(pageable).map(svcTargetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcTargetDTO> findOne(Long id) {
        log.debug("Request to get SvcTarget : {}", id);
        return svcTargetRepository.findById(id).map(svcTargetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcTarget : {}", id);
        svcTargetRepository.deleteById(id);
    }
}
