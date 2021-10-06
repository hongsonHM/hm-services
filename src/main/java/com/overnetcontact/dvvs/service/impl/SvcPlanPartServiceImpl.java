package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcPlanPart;
import com.overnetcontact.dvvs.repository.SvcPlanPartRepository;
import com.overnetcontact.dvvs.service.SvcPlanPartService;
import com.overnetcontact.dvvs.service.dto.SvcPlanPartDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanPartMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcPlanPart}.
 */
@Service
@Transactional
public class SvcPlanPartServiceImpl implements SvcPlanPartService {

    private final Logger log = LoggerFactory.getLogger(SvcPlanPartServiceImpl.class);

    private final SvcPlanPartRepository svcPlanPartRepository;

    private final SvcPlanPartMapper svcPlanPartMapper;

    public SvcPlanPartServiceImpl(SvcPlanPartRepository svcPlanPartRepository, SvcPlanPartMapper svcPlanPartMapper) {
        this.svcPlanPartRepository = svcPlanPartRepository;
        this.svcPlanPartMapper = svcPlanPartMapper;
    }

    @Override
    public SvcPlanPartDTO save(SvcPlanPartDTO svcPlanPartDTO) {
        log.debug("Request to save SvcPlanPart : {}", svcPlanPartDTO);
        SvcPlanPart svcPlanPart = svcPlanPartMapper.toEntity(svcPlanPartDTO);
        svcPlanPart = svcPlanPartRepository.save(svcPlanPart);
        return svcPlanPartMapper.toDto(svcPlanPart);
    }

    @Override
    public Optional<SvcPlanPartDTO> partialUpdate(SvcPlanPartDTO svcPlanPartDTO) {
        log.debug("Request to partially update SvcPlanPart : {}", svcPlanPartDTO);

        return svcPlanPartRepository
            .findById(svcPlanPartDTO.getId())
            .map(
                existingSvcPlanPart -> {
                    svcPlanPartMapper.partialUpdate(existingSvcPlanPart, svcPlanPartDTO);

                    return existingSvcPlanPart;
                }
            )
            .map(svcPlanPartRepository::save)
            .map(svcPlanPartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcPlanPartDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcPlanParts");
        return svcPlanPartRepository.findAll(pageable).map(svcPlanPartMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcPlanPartDTO> findOne(Long id) {
        log.debug("Request to get SvcPlanPart : {}", id);
        return svcPlanPartRepository.findById(id).map(svcPlanPartMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcPlanPart : {}", id);
        svcPlanPartRepository.deleteById(id);
    }
}
