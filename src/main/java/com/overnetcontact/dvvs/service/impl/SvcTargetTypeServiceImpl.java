package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcTargetType;
import com.overnetcontact.dvvs.repository.SvcTargetTypeRepository;
import com.overnetcontact.dvvs.service.SvcTargetTypeService;
import com.overnetcontact.dvvs.service.dto.SvcTargetTypeDTO;
import com.overnetcontact.dvvs.service.mapper.SvcTargetTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcTargetType}.
 */
@Service
@Transactional
public class SvcTargetTypeServiceImpl implements SvcTargetTypeService {

    private final Logger log = LoggerFactory.getLogger(SvcTargetTypeServiceImpl.class);

    private final SvcTargetTypeRepository svcTargetTypeRepository;

    private final SvcTargetTypeMapper svcTargetTypeMapper;

    public SvcTargetTypeServiceImpl(SvcTargetTypeRepository svcTargetTypeRepository, SvcTargetTypeMapper svcTargetTypeMapper) {
        this.svcTargetTypeRepository = svcTargetTypeRepository;
        this.svcTargetTypeMapper = svcTargetTypeMapper;
    }

    @Override
    public SvcTargetTypeDTO save(SvcTargetTypeDTO svcTargetTypeDTO) {
        log.debug("Request to save SvcTargetType : {}", svcTargetTypeDTO);
        SvcTargetType svcTargetType = svcTargetTypeMapper.toEntity(svcTargetTypeDTO);
        svcTargetType = svcTargetTypeRepository.save(svcTargetType);
        return svcTargetTypeMapper.toDto(svcTargetType);
    }

    @Override
    public Optional<SvcTargetTypeDTO> partialUpdate(SvcTargetTypeDTO svcTargetTypeDTO) {
        log.debug("Request to partially update SvcTargetType : {}", svcTargetTypeDTO);

        return svcTargetTypeRepository
            .findById(svcTargetTypeDTO.getId())
            .map(
                existingSvcTargetType -> {
                    svcTargetTypeMapper.partialUpdate(existingSvcTargetType, svcTargetTypeDTO);

                    return existingSvcTargetType;
                }
            )
            .map(svcTargetTypeRepository::save)
            .map(svcTargetTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcTargetTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcTargetTypes");
        return svcTargetTypeRepository.findAll(pageable).map(svcTargetTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcTargetTypeDTO> findOne(Long id) {
        log.debug("Request to get SvcTargetType : {}", id);
        return svcTargetTypeRepository.findById(id).map(svcTargetTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcTargetType : {}", id);
        svcTargetTypeRepository.deleteById(id);
    }
}
