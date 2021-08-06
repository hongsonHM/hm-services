package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.SvcContractService;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import com.overnetcontact.dvvs.service.mapper.SvcContractMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcContract}.
 */
@Service
@Transactional
public class SvcContractServiceImpl implements SvcContractService {

    private final Logger log = LoggerFactory.getLogger(SvcContractServiceImpl.class);

    private final SvcContractRepository svcContractRepository;

    private final SvcContractMapper svcContractMapper;

    public SvcContractServiceImpl(SvcContractRepository svcContractRepository, SvcContractMapper svcContractMapper) {
        this.svcContractRepository = svcContractRepository;
        this.svcContractMapper = svcContractMapper;
    }

    @Override
    public SvcContractDTO save(SvcContractDTO svcContractDTO) {
        log.debug("Request to save SvcContract : {}", svcContractDTO);
        SvcContract svcContract = svcContractMapper.toEntity(svcContractDTO);
        svcContract = svcContractRepository.save(svcContract);
        return svcContractMapper.toDto(svcContract);
    }

    @Override
    public Optional<SvcContractDTO> partialUpdate(SvcContractDTO svcContractDTO) {
        log.debug("Request to partially update SvcContract : {}", svcContractDTO);

        return svcContractRepository
            .findById(svcContractDTO.getId())
            .map(
                existingSvcContract -> {
                    svcContractMapper.partialUpdate(existingSvcContract, svcContractDTO);

                    return existingSvcContract;
                }
            )
            .map(svcContractRepository::save)
            .map(svcContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcContracts");
        return svcContractRepository.findAll(pageable).map(svcContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcContractDTO> findOne(Long id) {
        log.debug("Request to get SvcContract : {}", id);
        return svcContractRepository.findById(id).map(svcContractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcContract : {}", id);
        svcContractRepository.deleteById(id);
    }
}
