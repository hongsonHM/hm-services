package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcScheduleUnitRecord;
import com.overnetcontact.dvvs.repository.SvcScheduleUnitRecordRepository;
import com.overnetcontact.dvvs.service.criteria.SvcScheduleUnitRecordCriteria;
import com.overnetcontact.dvvs.service.dto.SvcScheduleUnitRecordDTO;
import com.overnetcontact.dvvs.service.mapper.SvcScheduleUnitRecordMapper;
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
 * Service for executing complex queries for {@link SvcScheduleUnitRecord} entities in the database.
 * The main input is a {@link SvcScheduleUnitRecordCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcScheduleUnitRecordDTO} or a {@link Page} of {@link SvcScheduleUnitRecordDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcScheduleUnitRecordQueryService extends QueryService<SvcScheduleUnitRecord> {

    private final Logger log = LoggerFactory.getLogger(SvcScheduleUnitRecordQueryService.class);

    private final SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository;

    private final SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper;

    public SvcScheduleUnitRecordQueryService(
        SvcScheduleUnitRecordRepository svcScheduleUnitRecordRepository,
        SvcScheduleUnitRecordMapper svcScheduleUnitRecordMapper
    ) {
        this.svcScheduleUnitRecordRepository = svcScheduleUnitRecordRepository;
        this.svcScheduleUnitRecordMapper = svcScheduleUnitRecordMapper;
    }

    /**
     * Return a {@link List} of {@link SvcScheduleUnitRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcScheduleUnitRecordDTO> findByCriteria(SvcScheduleUnitRecordCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcScheduleUnitRecord> specification = createSpecification(criteria);
        return svcScheduleUnitRecordMapper.toDto(svcScheduleUnitRecordRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcScheduleUnitRecordDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcScheduleUnitRecordDTO> findByCriteria(SvcScheduleUnitRecordCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcScheduleUnitRecord> specification = createSpecification(criteria);
        return svcScheduleUnitRecordRepository.findAll(specification, page).map(svcScheduleUnitRecordMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcScheduleUnitRecordCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcScheduleUnitRecord> specification = createSpecification(criteria);
        return svcScheduleUnitRecordRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcScheduleUnitRecordCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcScheduleUnitRecord> createSpecification(SvcScheduleUnitRecordCriteria criteria) {
        Specification<SvcScheduleUnitRecord> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcScheduleUnitRecord_.id));
            }
            if (criteria.getSvcScheduleUnitId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSvcScheduleUnitId(), SvcScheduleUnitRecord_.svcScheduleUnitId));
            }
            if (criteria.getIsPublished() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIsPublished(), SvcScheduleUnitRecord_.isPublished));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), SvcScheduleUnitRecord_.status));
            }
            if (criteria.getApproved() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApproved(), SvcScheduleUnitRecord_.approved));
            }
            if (criteria.getApprovedId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedId(), SvcScheduleUnitRecord_.approvedId));
            }
            if (criteria.getComment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComment(), SvcScheduleUnitRecord_.comment));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SvcScheduleUnitRecord_.createDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), SvcScheduleUnitRecord_.lastModifiedDate));
            }
            if (criteria.getCreateBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreateBy(), SvcScheduleUnitRecord_.createBy));
            }
            if (criteria.getCreateById() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateById(), SvcScheduleUnitRecord_.createById));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getLastModifiedBy(), SvcScheduleUnitRecord_.lastModifiedBy));
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedById(), SvcScheduleUnitRecord_.lastModifiedById));
            }
            if (criteria.getSvcSchedulePlanRecordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcSchedulePlanRecordId(),
                            root -> root.join(SvcScheduleUnitRecord_.svcSchedulePlanRecords, JoinType.LEFT).get(SvcSchedulePlanRecord_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
