package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcPlan;
import com.overnetcontact.dvvs.repository.SvcPlanRepository;
import com.overnetcontact.dvvs.service.SvcPlanService;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcPlan}.
 */
@Service
@Transactional
public class SvcPlanServiceImpl implements SvcPlanService {

    private final Logger log = LoggerFactory.getLogger(SvcPlanServiceImpl.class);

    private final SvcPlanRepository svcPlanRepository;

    private final SvcPlanMapper svcPlanMapper;

    public SvcPlanServiceImpl(SvcPlanRepository svcPlanRepository, SvcPlanMapper svcPlanMapper) {
        this.svcPlanRepository = svcPlanRepository;
        this.svcPlanMapper = svcPlanMapper;
    }

    @Override
    public SvcPlanDTO save(SvcPlanDTO svcPlanDTO) {
        log.debug("Request to save SvcPlan : {}", svcPlanDTO);
        SvcPlan svcPlan = svcPlanMapper.toEntity(svcPlanDTO);
        svcPlan = svcPlanRepository.save(svcPlan);
        return svcPlanMapper.toDto(svcPlan);
    }

    @Override
    public Optional<SvcPlanDTO> partialUpdate(SvcPlanDTO svcPlanDTO) {
        log.debug("Request to partially update SvcPlan : {}", svcPlanDTO);

        return svcPlanRepository
            .findById(svcPlanDTO.getId())
            .map(
                existingSvcPlan -> {
                    svcPlanMapper.partialUpdate(existingSvcPlan, svcPlanDTO);

                    return existingSvcPlan;
                }
            )
            .map(svcPlanRepository::save)
            .map(svcPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcPlans");
        return svcPlanRepository.findAll(pageable).map(svcPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcPlanDTO> findOne(Long id) {
        log.debug("Request to get SvcPlan : {}", id);
        return svcPlanRepository.findById(id).map(svcPlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcPlan : {}", id);
        svcPlanRepository.deleteById(id);
    }
}
