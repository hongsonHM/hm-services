package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcArea;
import com.overnetcontact.dvvs.repository.SvcAreaRepository;
import com.overnetcontact.dvvs.service.SvcAreaService;
import com.overnetcontact.dvvs.service.dto.SvcAreaDTO;
import com.overnetcontact.dvvs.service.mapper.SvcAreaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcArea}.
 */
@Service
@Transactional
public class SvcAreaServiceImpl implements SvcAreaService {

    private final Logger log = LoggerFactory.getLogger(SvcAreaServiceImpl.class);

    private final SvcAreaRepository svcAreaRepository;

    private final SvcAreaMapper svcAreaMapper;

    public SvcAreaServiceImpl(SvcAreaRepository svcAreaRepository, SvcAreaMapper svcAreaMapper) {
        this.svcAreaRepository = svcAreaRepository;
        this.svcAreaMapper = svcAreaMapper;
    }

    @Override
    public SvcAreaDTO save(SvcAreaDTO svcAreaDTO) {
        log.debug("Request to save SvcArea : {}", svcAreaDTO);
        SvcArea svcArea = svcAreaMapper.toEntity(svcAreaDTO);
        svcArea = svcAreaRepository.save(svcArea);
        return svcAreaMapper.toDto(svcArea);
    }

    @Override
    public Optional<SvcAreaDTO> partialUpdate(SvcAreaDTO svcAreaDTO) {
        log.debug("Request to partially update SvcArea : {}", svcAreaDTO);

        return svcAreaRepository
            .findById(svcAreaDTO.getId())
            .map(
                existingSvcArea -> {
                    svcAreaMapper.partialUpdate(existingSvcArea, svcAreaDTO);

                    return existingSvcArea;
                }
            )
            .map(svcAreaRepository::save)
            .map(svcAreaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SvcAreaDTO> findAll() {
        log.debug("Request to get all SvcAreas");
        return svcAreaRepository.findAll().stream().map(svcAreaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the svcAreas where SvcGroupTask is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SvcAreaDTO> findAllWhereSvcGroupTaskIsNull() {
        log.debug("Request to get all svcAreas where SvcGroupTask is null");
        return StreamSupport
            .stream(svcAreaRepository.findAll().spliterator(), false)
            .filter(svcArea -> svcArea.getSvcGroupTask() == null)
            .map(svcAreaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcAreaDTO> findOne(Long id) {
        log.debug("Request to get SvcArea : {}", id);
        return svcAreaRepository.findById(id).map(svcAreaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcArea : {}", id);
        svcAreaRepository.deleteById(id);
    }
}
