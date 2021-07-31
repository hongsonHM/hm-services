package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcClient;
import com.overnetcontact.dvvs.repository.SvcClientRepository;
import com.overnetcontact.dvvs.service.SvcClientService;
import com.overnetcontact.dvvs.service.dto.SvcClientDTO;
import com.overnetcontact.dvvs.service.mapper.SvcClientMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcClient}.
 */
@Service
@Transactional
public class SvcClientServiceImpl implements SvcClientService {

    private final Logger log = LoggerFactory.getLogger(SvcClientServiceImpl.class);

    private final SvcClientRepository svcClientRepository;

    private final SvcClientMapper svcClientMapper;

    public SvcClientServiceImpl(SvcClientRepository svcClientRepository, SvcClientMapper svcClientMapper) {
        this.svcClientRepository = svcClientRepository;
        this.svcClientMapper = svcClientMapper;
    }

    @Override
    public SvcClientDTO save(SvcClientDTO svcClientDTO) {
        log.debug("Request to save SvcClient : {}", svcClientDTO);
        SvcClient svcClient = svcClientMapper.toEntity(svcClientDTO);
        svcClient = svcClientRepository.save(svcClient);
        return svcClientMapper.toDto(svcClient);
    }

    @Override
    public Optional<SvcClientDTO> partialUpdate(SvcClientDTO svcClientDTO) {
        log.debug("Request to partially update SvcClient : {}", svcClientDTO);

        return svcClientRepository
            .findById(svcClientDTO.getId())
            .map(
                existingSvcClient -> {
                    svcClientMapper.partialUpdate(existingSvcClient, svcClientDTO);

                    return existingSvcClient;
                }
            )
            .map(svcClientRepository::save)
            .map(svcClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcClients");
        return svcClientRepository.findAll(pageable).map(svcClientMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcClientDTO> findOne(Long id) {
        log.debug("Request to get SvcClient : {}", id);
        return svcClientRepository.findById(id).map(svcClientMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcClient : {}", id);
        svcClientRepository.deleteById(id);
    }
}
