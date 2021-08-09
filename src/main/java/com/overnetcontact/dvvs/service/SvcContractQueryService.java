package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcContract;
import com.overnetcontact.dvvs.repository.SvcContractRepository;
import com.overnetcontact.dvvs.service.criteria.SvcContractCriteria;
import com.overnetcontact.dvvs.service.dto.SvcContractDTO;
import com.overnetcontact.dvvs.service.mapper.SvcContractMapper;
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
 * Service for executing complex queries for {@link SvcContract} entities in the database.
 * The main input is a {@link SvcContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcContractDTO} or a {@link Page} of {@link SvcContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcContractQueryService extends QueryService<SvcContract> {

    private final Logger log = LoggerFactory.getLogger(SvcContractQueryService.class);

    private final SvcContractRepository svcContractRepository;

    private final SvcContractMapper svcContractMapper;

    public SvcContractQueryService(SvcContractRepository svcContractRepository, SvcContractMapper svcContractMapper) {
        this.svcContractRepository = svcContractRepository;
        this.svcContractMapper = svcContractMapper;
    }

    /**
     * Return a {@link List} of {@link SvcContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcContractDTO> findByCriteria(SvcContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcContract> specification = createSpecification(criteria);
        return svcContractMapper.toDto(svcContractRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcContractDTO> findByCriteria(SvcContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcContract> specification = createSpecification(criteria);
        return svcContractRepository.findAll(specification, page).map(svcContractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcContractCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcContract> specification = createSpecification(criteria);
        return svcContractRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcContract> createSpecification(SvcContractCriteria criteria) {
        Specification<SvcContract> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcContract_.id));
            }
            if (criteria.getOrderNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderNumber(), SvcContract_.orderNumber));
            }
            if (criteria.getDocumentId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentId(), SvcContract_.documentId));
            }
            if (criteria.getAppendicesNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAppendicesNumber(), SvcContract_.appendicesNumber));
            }
            if (criteria.getFileId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileId(), SvcContract_.fileId));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), SvcContract_.content));
            }
            if (criteria.getEffectiveTimeFrom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEffectiveTimeFrom(), SvcContract_.effectiveTimeFrom));
            }
            if (criteria.getEffectiveTimeTo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEffectiveTimeTo(), SvcContract_.effectiveTimeTo));
            }
            if (criteria.getDurationMonth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationMonth(), SvcContract_.durationMonth));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), SvcContract_.value));
            }
            if (criteria.getContractValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractValue(), SvcContract_.contractValue));
            }
            if (criteria.getHumanResources() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHumanResources(), SvcContract_.humanResources));
            }
            if (criteria.getHumanResourcesWeekend() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getHumanResourcesWeekend(), SvcContract_.humanResourcesWeekend));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), SvcContract_.status));
            }
            if (criteria.getSubjectCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubjectCount(), SvcContract_.subjectCount));
            }
            if (criteria.getValuePerPerson() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValuePerPerson(), SvcContract_.valuePerPerson));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), SvcContract_.year));
            }
            if (criteria.getTargetsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTargetsId(),
                            root -> root.join(SvcContract_.targets, JoinType.LEFT).get(SvcTarget_.id)
                        )
                    );
            }
            if (criteria.getApprovedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApprovedById(),
                            root -> root.join(SvcContract_.approvedBy, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getOwnerById() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getOwnerById(), root -> root.join(SvcContract_.ownerBy, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUnitId(), root -> root.join(SvcContract_.unit, JoinType.LEFT).get(SvcUnit_.id))
                    );
            }
            if (criteria.getSalerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSalerId(), root -> root.join(SvcContract_.saler, JoinType.LEFT).get(OrgUser_.id))
                    );
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getClientId(), root -> root.join(SvcContract_.client, JoinType.LEFT).get(SvcClient_.id))
                    );
            }
        }
        return specification;
    }
}
