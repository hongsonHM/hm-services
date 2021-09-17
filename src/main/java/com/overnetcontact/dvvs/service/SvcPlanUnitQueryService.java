package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcPlanUnit;
import com.overnetcontact.dvvs.repository.SvcPlanUnitRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanUnitCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanUnitDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanUnitMapper;
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
 * Service for executing complex queries for {@link SvcPlanUnit} entities in the database.
 * The main input is a {@link SvcPlanUnitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcPlanUnitDTO} or a {@link Page} of {@link SvcPlanUnitDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcPlanUnitQueryService extends QueryService<SvcPlanUnit> {

    private final Logger log = LoggerFactory.getLogger(SvcPlanUnitQueryService.class);

    private final SvcPlanUnitRepository svcPlanUnitRepository;

    private final SvcPlanUnitMapper svcPlanUnitMapper;

    public SvcPlanUnitQueryService(SvcPlanUnitRepository svcPlanUnitRepository, SvcPlanUnitMapper svcPlanUnitMapper) {
        this.svcPlanUnitRepository = svcPlanUnitRepository;
        this.svcPlanUnitMapper = svcPlanUnitMapper;
    }

    /**
     * Return a {@link List} of {@link SvcPlanUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcPlanUnitDTO> findByCriteria(SvcPlanUnitCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcPlanUnit> specification = createSpecification(criteria);
        return svcPlanUnitMapper.toDto(svcPlanUnitRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcPlanUnitDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcPlanUnitDTO> findByCriteria(SvcPlanUnitCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcPlanUnit> specification = createSpecification(criteria);
        return svcPlanUnitRepository.findAll(specification, page).map(svcPlanUnitMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcPlanUnitCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcPlanUnit> specification = createSpecification(criteria);
        return svcPlanUnitRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcPlanUnitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcPlanUnit> createSpecification(SvcPlanUnitCriteria criteria) {
        Specification<SvcPlanUnit> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcPlanUnit_.id));
            }
            if (criteria.getStartAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartAt(), SvcPlanUnit_.startAt));
            }
            if (criteria.getEndAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndAt(), SvcPlanUnit_.endAt));
            }
            if (criteria.getCreateAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateAt(), SvcPlanUnit_.createAt));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), SvcPlanUnit_.status));
            }
            if (criteria.getAmountOfWork() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmountOfWork(), SvcPlanUnit_.amountOfWork));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), SvcPlanUnit_.quantity));
            }
            if (criteria.getFrequency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFrequency(), SvcPlanUnit_.frequency));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SvcPlanUnit_.note));
            }
            if (criteria.getSuppervisorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSuppervisorId(), SvcPlanUnit_.suppervisorId));
            }
            if (criteria.getSvcLaborId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcLaborId(),
                            root -> root.join(SvcPlanUnit_.svcLabors, JoinType.LEFT).get(SvcLabor_.id)
                        )
                    );
            }
            if (criteria.getSvcPlanTaskId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcPlanTaskId(),
                            root -> root.join(SvcPlanUnit_.svcPlanTasks, JoinType.LEFT).get(SvcPlanTask_.id)
                        )
                    );
            }
            if (criteria.getSvcPlanId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSvcPlanId(), root -> root.join(SvcPlanUnit_.svcPlan, JoinType.LEFT).get(SvcPlan_.id))
                    );
            }
        }
        return specification;
    }
}
