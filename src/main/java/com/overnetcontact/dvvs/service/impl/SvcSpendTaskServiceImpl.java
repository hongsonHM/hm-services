package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcSpendTask;
import com.overnetcontact.dvvs.repository.SvcSpendTaskRepository;
import com.overnetcontact.dvvs.service.SvcSpendTaskService;
import com.overnetcontact.dvvs.service.dto.SvcSpendTaskDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSpendTaskMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcSpendTask}.
 */
@Service
@Transactional
public class SvcSpendTaskServiceImpl implements SvcSpendTaskService {

    private final Logger log = LoggerFactory.getLogger(SvcSpendTaskServiceImpl.class);

    private final SvcSpendTaskRepository svcSpendTaskRepository;

    private final SvcSpendTaskMapper svcSpendTaskMapper;

    public SvcSpendTaskServiceImpl(SvcSpendTaskRepository svcSpendTaskRepository, SvcSpendTaskMapper svcSpendTaskMapper) {
        this.svcSpendTaskRepository = svcSpendTaskRepository;
        this.svcSpendTaskMapper = svcSpendTaskMapper;
    }

    @Override
    public SvcSpendTaskDTO save(SvcSpendTaskDTO svcSpendTaskDTO) {
        log.debug("Request to save SvcSpendTask : {}", svcSpendTaskDTO);
        SvcSpendTask svcSpendTask = svcSpendTaskMapper.toEntity(svcSpendTaskDTO);
        svcSpendTask = svcSpendTaskRepository.save(svcSpendTask);
        return svcSpendTaskMapper.toDto(svcSpendTask);
    }

    @Override
    public Optional<SvcSpendTaskDTO> partialUpdate(SvcSpendTaskDTO svcSpendTaskDTO) {
        log.debug("Request to partially update SvcSpendTask : {}", svcSpendTaskDTO);

        return svcSpendTaskRepository
            .findById(svcSpendTaskDTO.getId())
            .map(
                existingSvcSpendTask -> {
                    svcSpendTaskMapper.partialUpdate(existingSvcSpendTask, svcSpendTaskDTO);

                    return existingSvcSpendTask;
                }
            )
            .map(svcSpendTaskRepository::save)
            .map(svcSpendTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SvcSpendTaskDTO> findAll() {
        log.debug("Request to get all SvcSpendTasks");
        return svcSpendTaskRepository.findAll().stream().map(svcSpendTaskMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcSpendTaskDTO> findOne(Long id) {
        log.debug("Request to get SvcSpendTask : {}", id);
        return svcSpendTaskRepository.findById(id).map(svcSpendTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcSpendTask : {}", id);
        svcSpendTaskRepository.deleteById(id);
    }

    @Override
    public List<Object> findIds(List<Long> ids) {
        log.debug("request to find ids in list");
        return svcSpendTaskRepository.findByIds(ids);
    }
}
