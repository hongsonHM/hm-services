package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcPlanUnitRepository;
import com.overnetcontact.dvvs.service.SvcPlanUnitService;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanMapper;
import com.overnetcontact.dvvs.service.mapper.SvcPlanUnitMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcPlanUnit}.
 */
@Service
@Transactional
public class SvcPlanUnitServiceImpl implements SvcPlanUnitService {

    private final Logger log = LoggerFactory.getLogger(SvcPlanUnitServiceImpl.class);

    private final SvcPlanUnitRepository svcPlanUnitRepository;

    private final SvcPlanUnitMapper svcPlanUnitMapper;

    private final SvcPlanMapper svcPlanMapper;

    public SvcPlanUnitServiceImpl(
        SvcPlanUnitRepository svcPlanUnitRepository,
        SvcPlanUnitMapper svcPlanUnitMapper,
        SvcPlanMapper svcPlanMapper
    ) {
        this.svcPlanUnitRepository = svcPlanUnitRepository;
        this.svcPlanUnitMapper = svcPlanUnitMapper;
        this.svcPlanMapper = svcPlanMapper;
    }

    @Override
    public SvcPlanUnitDTO save(SvcPlanUnitDTO svcPlanUnitDTO) {
        log.debug("Request to save SvcPlanUnit : {}", svcPlanUnitDTO);
        SvcPlanUnit svcPlanUnit = svcPlanUnitMapper.toEntity(svcPlanUnitDTO);
        svcPlanUnit = svcPlanUnitRepository.save(svcPlanUnit);
        return svcPlanUnitMapper.toDto(svcPlanUnit);
    }

    @Override
    public Optional<SvcPlanUnitDTO> partialUpdate(SvcPlanUnitDTO svcPlanUnitDTO) {
        log.debug("Request to partially update SvcPlanUnit : {}", svcPlanUnitDTO);

        return svcPlanUnitRepository
            .findById(svcPlanUnitDTO.getId())
            .map(
                existingSvcPlanUnit -> {
                    svcPlanUnitMapper.partialUpdate(existingSvcPlanUnit, svcPlanUnitDTO);

                    return existingSvcPlanUnit;
                }
            )
            .map(svcPlanUnitRepository::save)
            .map(svcPlanUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcPlanUnitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcPlanUnits");
        return svcPlanUnitRepository.findAll(pageable).map(svcPlanUnitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcPlanUnitDTO> findOne(Long id) {
        log.debug("Request to get SvcPlanUnit : {}", id);
        return svcPlanUnitRepository.findById(id).map(svcPlanUnitMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcPlanUnit : {}", id);
        svcPlanUnitRepository.deleteById(id);
    }

    @Override
    public List<SvcPlanUnitDTO> findBySvcPlan(SvcPlanDTO svcPlanDTO) {
        log.debug("Request to find by plan : {}", svcPlanDTO);
        return svcPlanUnitRepository
            .findBySvcPlan(svcPlanMapper.toEntity(svcPlanDTO))
            .stream()
            .map(svcPlanUnitMapper::toDto)
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
