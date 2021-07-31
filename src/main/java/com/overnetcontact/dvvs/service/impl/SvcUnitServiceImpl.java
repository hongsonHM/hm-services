package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcUnit;
import com.overnetcontact.dvvs.repository.SvcUnitRepository;
import com.overnetcontact.dvvs.service.SvcUnitService;
import com.overnetcontact.dvvs.service.dto.SvcUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcUnitMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcUnit}.
 */
@Service
@Transactional
public class SvcUnitServiceImpl implements SvcUnitService {

    private final Logger log = LoggerFactory.getLogger(SvcUnitServiceImpl.class);

    private final SvcUnitRepository svcUnitRepository;

    private final SvcUnitMapper svcUnitMapper;

    public SvcUnitServiceImpl(SvcUnitRepository svcUnitRepository, SvcUnitMapper svcUnitMapper) {
        this.svcUnitRepository = svcUnitRepository;
        this.svcUnitMapper = svcUnitMapper;
    }

    @Override
    public SvcUnitDTO save(SvcUnitDTO svcUnitDTO) {
        log.debug("Request to save SvcUnit : {}", svcUnitDTO);
        SvcUnit svcUnit = svcUnitMapper.toEntity(svcUnitDTO);
        svcUnit = svcUnitRepository.save(svcUnit);
        return svcUnitMapper.toDto(svcUnit);
    }

    @Override
    public Optional<SvcUnitDTO> partialUpdate(SvcUnitDTO svcUnitDTO) {
        log.debug("Request to partially update SvcUnit : {}", svcUnitDTO);

        return svcUnitRepository
            .findById(svcUnitDTO.getId())
            .map(
                existingSvcUnit -> {
                    svcUnitMapper.partialUpdate(existingSvcUnit, svcUnitDTO);

                    return existingSvcUnit;
                }
            )
            .map(svcUnitRepository::save)
            .map(svcUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcUnits");
        return svcUnitRepository.findAll(pageable).map(svcUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcUnitDTO> findOne(Long id) {
        log.debug("Request to get SvcUnit : {}", id);
        return svcUnitRepository.findById(id).map(svcUnitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcUnit : {}", id);
        svcUnitRepository.deleteById(id);
    }
}
