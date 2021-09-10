package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord;
import com.overnetcontact.dvvs.repository.SvcSchedulePlanRecordRepository;
import com.overnetcontact.dvvs.service.SvcSchedulePlanRecordService;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSchedulePlanRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcSchedulePlanRecord}.
 */
@Service
@Transactional
public class SvcSchedulePlanRecordServiceImpl implements SvcSchedulePlanRecordService {

    private final Logger log = LoggerFactory.getLogger(SvcSchedulePlanRecordServiceImpl.class);

    private final SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository;

    private final SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper;

    public SvcSchedulePlanRecordServiceImpl(
        SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository,
        SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper
    ) {
        this.svcSchedulePlanRecordRepository = svcSchedulePlanRecordRepository;
        this.svcSchedulePlanRecordMapper = svcSchedulePlanRecordMapper;
    }

    @Override
    public SvcSchedulePlanRecordDTO save(SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO) {
        log.debug("Request to save SvcSchedulePlanRecord : {}", svcSchedulePlanRecordDTO);
        SvcSchedulePlanRecord svcSchedulePlanRecord = svcSchedulePlanRecordMapper.toEntity(svcSchedulePlanRecordDTO);
        svcSchedulePlanRecord = svcSchedulePlanRecordRepository.save(svcSchedulePlanRecord);
        return svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecord);
    }

    @Override
    public Optional<SvcSchedulePlanRecordDTO> partialUpdate(SvcSchedulePlanRecordDTO svcSchedulePlanRecordDTO) {
        log.debug("Request to partially update SvcSchedulePlanRecord : {}", svcSchedulePlanRecordDTO);

        return svcSchedulePlanRecordRepository
            .findById(svcSchedulePlanRecordDTO.getId())
            .map(
                existingSvcSchedulePlanRecord -> {
                    svcSchedulePlanRecordMapper.partialUpdate(existingSvcSchedulePlanRecord, svcSchedulePlanRecordDTO);

                    return existingSvcSchedulePlanRecord;
                }
            )
            .map(svcSchedulePlanRecordRepository::save)
            .map(svcSchedulePlanRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcSchedulePlanRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcSchedulePlanRecords");
        return svcSchedulePlanRecordRepository.findAll(pageable).map(svcSchedulePlanRecordMapper::toDto);
    }

    public Page<SvcSchedulePlanRecordDTO> findAllWithEagerRelationships(Pageable pageable) {
        return svcSchedulePlanRecordRepository.findAllWithEagerRelationships(pageable).map(svcSchedulePlanRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcSchedulePlanRecordDTO> findOne(Long id) {
        log.debug("Request to get SvcSchedulePlanRecord : {}", id);
        return svcSchedulePlanRecordRepository.findOneWithEagerRelationships(id).map(svcSchedulePlanRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcSchedulePlanRecord : {}", id);
        svcSchedulePlanRecordRepository.deleteById(id);
    }
}
