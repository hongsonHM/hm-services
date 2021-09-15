package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.CoreSupplies;
import com.overnetcontact.dvvs.repository.CoreSuppliesRepository;
import com.overnetcontact.dvvs.service.CoreSuppliesService;
import com.overnetcontact.dvvs.service.dto.CoreSuppliesDTO;
import com.overnetcontact.dvvs.service.mapper.CoreSuppliesMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CoreSupplies}.
 */
@Service
@Transactional
public class CoreSuppliesServiceImpl implements CoreSuppliesService {

    private final Logger log = LoggerFactory.getLogger(CoreSuppliesServiceImpl.class);

    private final CoreSuppliesRepository coreSuppliesRepository;

    private final CoreSuppliesMapper coreSuppliesMapper;

    public CoreSuppliesServiceImpl(CoreSuppliesRepository coreSuppliesRepository, CoreSuppliesMapper coreSuppliesMapper) {
        this.coreSuppliesRepository = coreSuppliesRepository;
        this.coreSuppliesMapper = coreSuppliesMapper;
    }

    @Override
    public CoreSuppliesDTO save(CoreSuppliesDTO coreSuppliesDTO) {
        log.debug("Request to save CoreSupplies : {}", coreSuppliesDTO);
        CoreSupplies coreSupplies = coreSuppliesMapper.toEntity(coreSuppliesDTO);
        coreSupplies = coreSuppliesRepository.save(coreSupplies);
        return coreSuppliesMapper.toDto(coreSupplies);
    }

    @Override
    public Optional<CoreSuppliesDTO> partialUpdate(CoreSuppliesDTO coreSuppliesDTO) {
        log.debug("Request to partially update CoreSupplies : {}", coreSuppliesDTO);

        return coreSuppliesRepository
            .findById(coreSuppliesDTO.getId())
            .map(
                existingCoreSupplies -> {
                    coreSuppliesMapper.partialUpdate(existingCoreSupplies, coreSuppliesDTO);

                    return existingCoreSupplies;
                }
            )
            .map(coreSuppliesRepository::save)
            .map(coreSuppliesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoreSuppliesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CoreSupplies");
        return coreSuppliesRepository.findAll(pageable).map(coreSuppliesMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoreSuppliesDTO> findOne(Long id) {
        log.debug("Request to get CoreSupplies : {}", id);
        return coreSuppliesRepository.findById(id).map(coreSuppliesMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoreSupplies : {}", id);
        coreSuppliesRepository.deleteById(id);
    }
}
