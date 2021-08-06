package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcSchedulePlan;
import com.overnetcontact.dvvs.repository.SvcSchedulePlanRepository;
import com.overnetcontact.dvvs.service.SvcSchedulePlanService;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSchedulePlanMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcSchedulePlan}.
 */
@Service
@Transactional
public class SvcSchedulePlanServiceImpl implements SvcSchedulePlanService {

    private final Logger log = LoggerFactory.getLogger(SvcSchedulePlanServiceImpl.class);

    private final SvcSchedulePlanRepository svcSchedulePlanRepository;

    private final SvcSchedulePlanMapper svcSchedulePlanMapper;

    public SvcSchedulePlanServiceImpl(SvcSchedulePlanRepository svcSchedulePlanRepository, SvcSchedulePlanMapper svcSchedulePlanMapper) {
        this.svcSchedulePlanRepository = svcSchedulePlanRepository;
        this.svcSchedulePlanMapper = svcSchedulePlanMapper;
    }

    @Override
    public SvcSchedulePlanDTO save(SvcSchedulePlanDTO svcSchedulePlanDTO) {
        log.debug("Request to save SvcSchedulePlan : {}", svcSchedulePlanDTO);
        SvcSchedulePlan svcSchedulePlan = svcSchedulePlanMapper.toEntity(svcSchedulePlanDTO);
        svcSchedulePlan = svcSchedulePlanRepository.save(svcSchedulePlan);
        return svcSchedulePlanMapper.toDto(svcSchedulePlan);
    }

    @Override
    public Optional<SvcSchedulePlanDTO> partialUpdate(SvcSchedulePlanDTO svcSchedulePlanDTO) {
        log.debug("Request to partially update SvcSchedulePlan : {}", svcSchedulePlanDTO);

        return svcSchedulePlanRepository
            .findById(svcSchedulePlanDTO.getId())
            .map(
                existingSvcSchedulePlan -> {
                    svcSchedulePlanMapper.partialUpdate(existingSvcSchedulePlan, svcSchedulePlanDTO);

                    return existingSvcSchedulePlan;
                }
            )
            .map(svcSchedulePlanRepository::save)
            .map(svcSchedulePlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcSchedulePlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcSchedulePlans");
        return svcSchedulePlanRepository.findAll(pageable).map(svcSchedulePlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcSchedulePlanDTO> findOne(Long id) {
        log.debug("Request to get SvcSchedulePlan : {}", id);
        return svcSchedulePlanRepository.findById(id).map(svcSchedulePlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcSchedulePlan : {}", id);
        svcSchedulePlanRepository.deleteById(id);
    }
}
