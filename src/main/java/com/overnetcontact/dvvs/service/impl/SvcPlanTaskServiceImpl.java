package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcPlanTask;
import com.overnetcontact.dvvs.repository.SvcPlanTaskRepository;
import com.overnetcontact.dvvs.service.SvcPlanTaskService;
import com.overnetcontact.dvvs.service.dto.SvcPlanTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanTaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcPlanTask}.
 */
@Service
@Transactional
public class SvcPlanTaskServiceImpl implements SvcPlanTaskService {

    private final Logger log = LoggerFactory.getLogger(SvcPlanTaskServiceImpl.class);

    private final SvcPlanTaskRepository svcPlanTaskRepository;

    private final SvcPlanTaskMapper svcPlanTaskMapper;

    public SvcPlanTaskServiceImpl(SvcPlanTaskRepository svcPlanTaskRepository, SvcPlanTaskMapper svcPlanTaskMapper) {
        this.svcPlanTaskRepository = svcPlanTaskRepository;
        this.svcPlanTaskMapper = svcPlanTaskMapper;
    }

    @Override
    public SvcPlanTaskDTO save(SvcPlanTaskDTO svcPlanTaskDTO) {
        log.debug("Request to save SvcPlanTask : {}", svcPlanTaskDTO);
        SvcPlanTask svcPlanTask = svcPlanTaskMapper.toEntity(svcPlanTaskDTO);
        svcPlanTask = svcPlanTaskRepository.save(svcPlanTask);
        return svcPlanTaskMapper.toDto(svcPlanTask);
    }

    @Override
    public Optional<SvcPlanTaskDTO> partialUpdate(SvcPlanTaskDTO svcPlanTaskDTO) {
        log.debug("Request to partially update SvcPlanTask : {}", svcPlanTaskDTO);

        return svcPlanTaskRepository
            .findById(svcPlanTaskDTO.getId())
            .map(
                existingSvcPlanTask -> {
                    svcPlanTaskMapper.partialUpdate(existingSvcPlanTask, svcPlanTaskDTO);

                    return existingSvcPlanTask;
                }
            )
            .map(svcPlanTaskRepository::save)
            .map(svcPlanTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcPlanTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcPlanTasks");
        return svcPlanTaskRepository.findAll(pageable).map(svcPlanTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcPlanTaskDTO> findOne(Long id) {
        log.debug("Request to get SvcPlanTask : {}", id);
        return svcPlanTaskRepository.findById(id).map(svcPlanTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcPlanTask : {}", id);
        svcPlanTaskRepository.deleteById(id);
    }
}
