package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcGroupTask;
import com.overnetcontact.dvvs.repository.SvcGroupTaskRepository;
import com.overnetcontact.dvvs.service.SvcGroupTaskService;
import com.overnetcontact.dvvs.service.dto.SvcGroupTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcGroupTaskMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcGroupTask}.
 */
@Service
@Transactional
public class SvcGroupTaskServiceImpl implements SvcGroupTaskService {

    private final Logger log = LoggerFactory.getLogger(SvcGroupTaskServiceImpl.class);

    private final SvcGroupTaskRepository svcGroupTaskRepository;

    private final SvcGroupTaskMapper svcGroupTaskMapper;

    public SvcGroupTaskServiceImpl(SvcGroupTaskRepository svcGroupTaskRepository, SvcGroupTaskMapper svcGroupTaskMapper) {
        this.svcGroupTaskRepository = svcGroupTaskRepository;
        this.svcGroupTaskMapper = svcGroupTaskMapper;
    }

    @Override
    public SvcGroupTaskDTO save(SvcGroupTaskDTO svcGroupTaskDTO) {
        log.debug("Request to save SvcGroupTask : {}", svcGroupTaskDTO);
        SvcGroupTask svcGroupTask = svcGroupTaskMapper.toEntity(svcGroupTaskDTO);
        svcGroupTask = svcGroupTaskRepository.save(svcGroupTask);
        return svcGroupTaskMapper.toDto(svcGroupTask);
    }

    @Override
    public Optional<SvcGroupTaskDTO> partialUpdate(SvcGroupTaskDTO svcGroupTaskDTO) {
        log.debug("Request to partially update SvcGroupTask : {}", svcGroupTaskDTO);

        return svcGroupTaskRepository
            .findById(svcGroupTaskDTO.getId())
            .map(
                existingSvcGroupTask -> {
                    svcGroupTaskMapper.partialUpdate(existingSvcGroupTask, svcGroupTaskDTO);

                    return existingSvcGroupTask;
                }
            )
            .map(svcGroupTaskRepository::save)
            .map(svcGroupTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SvcGroupTaskDTO> findAll() {
        log.debug("Request to get all SvcGroupTasks");
        return svcGroupTaskRepository.findAll().stream().map(svcGroupTaskMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcGroupTaskDTO> findOne(Long id) {
        log.debug("Request to get SvcGroupTask : {}", id);
        return svcGroupTaskRepository.findById(id).map(svcGroupTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcGroupTask : {}", id);
        svcGroupTaskRepository.deleteById(id);
    }
}
