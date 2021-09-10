package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcSchedulePlanRecord;
import com.overnetcontact.dvvs.repository.SvcSchedulePlanRecordRepository;
import com.overnetcontact.dvvs.service.criteria.SvcSchedulePlanRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcSchedulePlanRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcSchedulePlanRecordMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SvcSchedulePlanRecord} entities in the database.
 * The main input is a {@link SvcSchedulePlanRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcSchedulePlanRecordDTO} or a {@link Page} of {@link SvcSchedulePlanRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcSchedulePlanRecordQueryService extends QueryService<SvcSchedulePlanRecord> {

    private final Logger log = LoggerFactory.getLogger(SvcSchedulePlanRecordQueryService.class);

    private final SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository;

    private final SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper;

    public SvcSchedulePlanRecordQueryService(
        SvcSchedulePlanRecordRepository svcSchedulePlanRecordRepository,
        SvcSchedulePlanRecordMapper svcSchedulePlanRecordMapper
    ) {
        this.svcSchedulePlanRecordRepository = svcSchedulePlanRecordRepository;
        this.svcSchedulePlanRecordMapper = svcSchedulePlanRecordMapper;
    }

    /**
     * Return a {@link List} of {@link SvcSchedulePlanRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcSchedulePlanRecordDTO> findByCriteria(SvcSchedulePlanRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcSchedulePlanRecord> specification = createSpecification(criteria);
        return svcSchedulePlanRecordMapper.toDto(svcSchedulePlanRecordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcSchedulePlanRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcSchedulePlanRecordDTO> findByCriteria(SvcSchedulePlanRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcSchedulePlanRecord> specification = createSpecification(criteria);
        return svcSchedulePlanRecordRepository.findAll(specification, page).map(svcSchedulePlanRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcSchedulePlanRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcSchedulePlanRecord> specification = createSpecification(criteria);
        return svcSchedulePlanRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcSchedulePlanRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcSchedulePlanRecord> createSpecification(SvcSchedulePlanRecordCriteria criteria) {
        Specification<SvcSchedulePlanRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcSchedulePlanRecord_.id));
            }
            if (criteria.getSvcSchedulePlanId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSvcSchedulePlanId(), SvcSchedulePlanRecord_.svcSchedulePlanId));
            }
            if (criteria.getIsPublished() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsPublished(), SvcSchedulePlanRecord_.isPublished));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), SvcSchedulePlanRecord_.status));
            }
            if (criteria.getApproved() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApproved(), SvcSchedulePlanRecord_.approved));
            }
            if (criteria.getApprovedId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedId(), SvcSchedulePlanRecord_.approvedId));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), SvcSchedulePlanRecord_.comment));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SvcSchedulePlanRecord_.createDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), SvcSchedulePlanRecord_.lastModifiedDate));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), SvcSchedulePlanRecord_.createBy));
            }
            if (criteria.getCreateById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateById(), SvcSchedulePlanRecord_.createById));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), SvcSchedulePlanRecord_.lastModifiedBy));
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedById(), SvcSchedulePlanRecord_.lastModifiedById));
            }
            if (criteria.getSvcScheduleUnitRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcScheduleUnitRecordId(),
                            root -> root.join(SvcSchedulePlanRecord_.svcScheduleUnitRecords, JoinType.LEFT).get(SvcScheduleUnitRecord_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
