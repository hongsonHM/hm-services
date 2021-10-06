package com.overnetcontact.dvvs.service.impl;

import com.overnetcontact.dvvs.domain.*;
import com.overnetcontact.dvvs.domain.enumeration.NotificationStatus;
import com.overnetcontact.dvvs.repository.*;
import com.overnetcontact.dvvs.service.SvcPlanService;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SvcPlan}.
 */
@Service
@Transactional
public class SvcPlanServiceImpl implements SvcPlanService {

    private final String PHONG_GIAM_SAT = "Phòng Giám Sát";

    private final Logger log = LoggerFactory.getLogger(SvcPlanServiceImpl.class);

    private final SvcPlanRepository svcPlanRepository;

    private final SvcPlanMapper svcPlanMapper;

    private final OrgUserRepository orgUserRepository;

    private final SvcGroupRepository svcGroupRepository;

    private final OrgGroupRepository orgGroupRepository;

    private final OrgNotificationRepository orgNotificationRepository;

    public SvcPlanServiceImpl(
        SvcPlanRepository svcPlanRepository,
        SvcPlanMapper svcPlanMapper,
        OrgUserRepository orgUserRepository,
        SvcGroupRepository svcGroupRepository,
        OrgGroupRepository orgGroupRepository,
        OrgNotificationRepository orgNotificationRepository
    ) {
        this.svcPlanRepository = svcPlanRepository;
        this.svcPlanMapper = svcPlanMapper;
        this.orgUserRepository = orgUserRepository;
        this.svcGroupRepository = svcGroupRepository;
        this.orgGroupRepository = orgGroupRepository;
        this.orgNotificationRepository = orgNotificationRepository;
    }

    @Override
    public SvcPlanDTO save(SvcPlanDTO svcPlanDTO) {
        log.debug("Request to save SvcPlan : {}", svcPlanDTO);
        SvcPlan svcPlan = svcPlanMapper.toEntity(svcPlanDTO);
        svcPlan = svcPlanRepository.save(svcPlan);
        Optional<OrgGroup> orgGroup = orgGroupRepository.findByName(PHONG_GIAM_SAT);
        List<OrgUser> orgUsers = orgUserRepository.findByGroup(orgGroup.get());

        for (OrgUser orgUser : orgUsers) {
            OrgNotification orgNotification = new OrgNotification();
            orgNotification.setStatus(NotificationStatus.PROCESS);
            orgNotification.setOrgUser(orgUser);
            orgNotification.setIsRead(false);
            orgNotification.setTitle("Hợp kế hoạch được tạo! Đang chờ phê duyệt");
            orgNotification.setDesc("Một kế hoạch mới đã được tạo! Đang chờ phê duyệt");
            orgNotification.setData(String.valueOf(svcPlan.getId()) + "| plan");
            orgNotificationRepository.save(orgNotification);
        }
        return svcPlanMapper.toDto(svcPlan);
    }

    @Override
    public Optional<SvcPlanDTO> partialUpdate(SvcPlanDTO svcPlanDTO) {
        log.debug("Request to partially update SvcPlan : {}", svcPlanDTO);

        return svcPlanRepository
            .findById(svcPlanDTO.getId())
            .map(
                existingSvcPlan -> {
                    svcPlanMapper.partialUpdate(existingSvcPlan, svcPlanDTO);

                    return existingSvcPlan;
                }
            )
            .map(svcPlanRepository::save)
            .map(svcPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SvcPlanDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SvcPlans");
        return svcPlanRepository.findAll(pageable).map(svcPlanMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SvcPlanDTO> findOne(Long id) {
        log.debug("Request to get SvcPlan : {}", id);
        return svcPlanRepository.findById(id).map(svcPlanMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SvcPlan : {}", id);
        svcPlanRepository.deleteById(id);
    }
}
