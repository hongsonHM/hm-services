package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcLabor;
import com.overnetcontact.dvvs.repository.SvcLaborRepository;
import com.overnetcontact.dvvs.service.SvcLaborService;
import com.overnetcontact.dvvs.service.dto.SvcLaborDTO;
import com.overnetcontact.dvvs.service.mapper.SvcLaborMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcLabor}.
 */
@Service
@Transactional
public class SvcLaborServiceImpl implements SvcLaborService {

    private final Logger log = LoggerFactory.getLogger(SvcLaborServiceImpl.class);

    private final SvcLaborRepository svcLaborRepository;

    private final SvcLaborMapper svcLaborMapper;

    public SvcLaborServiceImpl(SvcLaborRepository svcLaborRepository, SvcLaborMapper svcLaborMapper) {
        this.svcLaborRepository = svcLaborRepository;
        this.svcLaborMapper = svcLaborMapper;
    }

    @Override
    public SvcLaborDTO save(SvcLaborDTO svcLaborDTO) {
        log.debug("Request to save SvcLabor : {}", svcLaborDTO);
        SvcLabor svcLabor = svcLaborMapper.toEntity(svcLaborDTO);
        svcLabor = svcLaborRepository.save(svcLabor);
        return svcLaborMapper.toDto(svcLabor);
    }

    @Override
    public Optional<SvcLaborDTO> partialUpdate(SvcLaborDTO svcLaborDTO) {
        log.debug("Request to partially update SvcLabor : {}", svcLaborDTO);

        return svcLaborRepository
            .findById(svcLaborDTO.getId())
            .map(
                existingSvcLabor -> {
                    svcLaborMapper.partialUpdate(existingSvcLabor, svcLaborDTO);

                    return existingSvcLabor;
                }
            )
            .map(svcLaborRepository::save)
            .map(svcLaborMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcLaborDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcLabors");
        return svcLaborRepository.findAll(pageable).map(svcLaborMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcLaborDTO> findOne(Long id) {
        log.debug("Request to get SvcLabor : {}", id);
        return svcLaborRepository.findById(id).map(svcLaborMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcLabor : {}", id);
        svcLaborRepository.deleteById(id);
    }
}
