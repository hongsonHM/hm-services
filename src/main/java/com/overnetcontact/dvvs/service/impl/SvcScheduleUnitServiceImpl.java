package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcScheduleUnit;
import com.overnetcontact.dvvs.repository.SvcScheduleUnitRepository;
import com.overnetcontact.dvvs.service.SvcScheduleUnitService;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcScheduleUnitMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcScheduleUnit}.
 */
@Service
@Transactional
public class SvcScheduleUnitServiceImpl implements SvcScheduleUnitService {

    private final Logger log = LoggerFactory.getLogger(SvcScheduleUnitServiceImpl.class);

    private final SvcScheduleUnitRepository svcScheduleUnitRepository;

    private final SvcScheduleUnitMapper svcScheduleUnitMapper;

    public SvcScheduleUnitServiceImpl(SvcScheduleUnitRepository svcScheduleUnitRepository, SvcScheduleUnitMapper svcScheduleUnitMapper) {
        this.svcScheduleUnitRepository = svcScheduleUnitRepository;
        this.svcScheduleUnitMapper = svcScheduleUnitMapper;
    }

    @Override
    public SvcScheduleUnitDTO save(SvcScheduleUnitDTO svcScheduleUnitDTO) {
        log.debug("Request to save SvcScheduleUnit : {}", svcScheduleUnitDTO);
        SvcScheduleUnit svcScheduleUnit = svcScheduleUnitMapper.toEntity(svcScheduleUnitDTO);
        svcScheduleUnit = svcScheduleUnitRepository.save(svcScheduleUnit);
        return svcScheduleUnitMapper.toDto(svcScheduleUnit);
    }

    @Override
    public Optional<SvcScheduleUnitDTO> partialUpdate(SvcScheduleUnitDTO svcScheduleUnitDTO) {
        log.debug("Request to partially update SvcScheduleUnit : {}", svcScheduleUnitDTO);

        return svcScheduleUnitRepository
            .findById(svcScheduleUnitDTO.getId())
            .map(
                existingSvcScheduleUnit -> {
                    svcScheduleUnitMapper.partialUpdate(existingSvcScheduleUnit, svcScheduleUnitDTO);

                    return existingSvcScheduleUnit;
                }
            )
            .map(svcScheduleUnitRepository::save)
            .map(svcScheduleUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcScheduleUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcScheduleUnits");
        return svcScheduleUnitRepository.findAll(pageable).map(svcScheduleUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcScheduleUnitDTO> findOne(Long id) {
        log.debug("Request to get SvcScheduleUnit : {}", id);
        return svcScheduleUnitRepository.findById(id).map(svcScheduleUnitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcScheduleUnit : {}", id);
        svcScheduleUnitRepository.deleteById(id);
    }
}
