package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord;
import com.overnetcontact.dvvs.repository.SvcScheduleUnitRecordRepository;
import com.overnetcontact.dvvs.service.SvcScheduleUnitRecordService;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcScheduleUnitRecordMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcScheduleUnitRecord}.
 */
@Service
@Transactional
public class SvcScheduleUnitRecordServiceImpl implements SvcScheduleUnitRecordService {

    private final Logger log = LoggerFactory.getLogger(SvcScheduleUnitRecordServiceImpl.class);

    private final SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository;

    private final SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper;

    public SvcScheduleUnitRecordServiceImpl(
        SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository,
        SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper
    ) {
        this.svcScheduleUnitRecordRepository = svcScheduleUnitRecordRepository;
        this.svcScheduleUnitRecordMapper = svcScheduleUnitRecordMapper;
    }

    @Override
    public SvcScheduleUnitRecordDTO save(SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO) {
        log.debug("Request to save SvcScheduleUnitRecord : {}", svcScheduleUnitRecordDTO);
        SvcScheduleUnitRecord svcScheduleUnitRecord = svcScheduleUnitRecordMapper.toEntity(svcScheduleUnitRecordDTO);
        svcScheduleUnitRecord = svcScheduleUnitRecordRepository.save(svcScheduleUnitRecord);
        return svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecord);
    }

    @Override
    public Optional<SvcScheduleUnitRecordDTO> partialUpdate(SvcScheduleUnitRecordDTO svcScheduleUnitRecordDTO) {
        log.debug("Request to partially update SvcScheduleUnitRecord : {}", svcScheduleUnitRecordDTO);

        return svcScheduleUnitRecordRepository
            .findById(svcScheduleUnitRecordDTO.getId())
            .map(
                existingSvcScheduleUnitRecord -> {
                    svcScheduleUnitRecordMapper.partialUpdate(existingSvcScheduleUnitRecord, svcScheduleUnitRecordDTO);

                    return existingSvcScheduleUnitRecord;
                }
            )
            .map(svcScheduleUnitRecordRepository::save)
            .map(svcScheduleUnitRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcScheduleUnitRecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcScheduleUnitRecords");
        return svcScheduleUnitRecordRepository.findAll(pageable).map(svcScheduleUnitRecordMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcScheduleUnitRecordDTO> findOne(Long id) {
        log.debug("Request to get SvcScheduleUnitRecord : {}", id);
        return svcScheduleUnitRecordRepository.findById(id).map(svcScheduleUnitRecordMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcScheduleUnitRecord : {}", id);
        svcScheduleUnitRecordRepository.deleteById(id);
    }
}
