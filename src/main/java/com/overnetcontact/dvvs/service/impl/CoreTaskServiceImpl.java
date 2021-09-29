package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.CoreTask;
import com.overnetcontact.dvvs.repository.CoreTaskRepository;
import com.overnetcontact.dvvs.service.CoreTaskService;
import com.overnetcontact.dvvs.service.dto.CoreTaskDTO;
import com.overnetcontact.dvvs.service.mapper.CoreTaskMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CoreTask}.
 */
@Service
@Transactional
public class CoreTaskServiceImpl implements CoreTaskService {

    private final Logger log = LoggerFactory.getLogger(CoreTaskServiceImpl.class);

    private final CoreTaskRepository coreTaskRepository;

    private final CoreTaskMapper coreTaskMapper;

    public CoreTaskServiceImpl(CoreTaskRepository coreTaskRepository, CoreTaskMapper coreTaskMapper) {
        this.coreTaskRepository = coreTaskRepository;
        this.coreTaskMapper = coreTaskMapper;
    }

    @Override
    public CoreTaskDTO save(CoreTaskDTO coreTaskDTO) {
        log.debug("Request to save CoreTask : {}", coreTaskDTO);
        CoreTask coreTask = coreTaskMapper.toEntity(coreTaskDTO);
        coreTask = coreTaskRepository.save(coreTask);
        return coreTaskMapper.toDto(coreTask);
    }

    @Override
    public Optional<CoreTaskDTO> partialUpdate(CoreTaskDTO coreTaskDTO) {
        log.debug("Request to partially update CoreTask : {}", coreTaskDTO);

        return coreTaskRepository
            .findById(coreTaskDTO.getId())
            .map(
                existingCoreTask -> {
                    coreTaskMapper.partialUpdate(existingCoreTask, coreTaskDTO);

                    return existingCoreTask;
                }
            )
            .map(coreTaskRepository::save)
            .map(coreTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoreTaskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CoreTasks");
        return coreTaskRepository.findAll(pageable).map(coreTaskMapper::toDto);
    }

    public Page<CoreTaskDTO> findAllWithEagerRelationships(Pageable pageable) {
        return coreTaskRepository.findAllWithEagerRelationships(pageable).map(coreTaskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoreTaskDTO> findOne(Long id) {
        log.debug("Request to get CoreTask : {}", id);
        return coreTaskRepository.findOneWithEagerRelationships(id).map(coreTaskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoreTask : {}", id);
        coreTaskRepository.deleteById(id);
    }

    @Override
    public List<CoreTask> findByIdIn(Set<Long> inventoryIdList) {
        return coreTaskRepository.findByIdIn(inventoryIdList);
    }

    @Override
    public List<Object> findSuppliesWithTask(List<Long> ids) {
        return coreTaskRepository.findSuppliesWithTask(ids);
    }
}
