package com.overnetcontact.dvvs.service;

import com.overnetcontact.dvvs.domain.*; // for static metamodels
import com.overnetcontact.dvvs.domain.SvcPlan;
import com.overnetcontact.dvvs.repository.SvcPlanRepository;
import com.overnetcontact.dvvs.service.criteria.SvcPlanCriteria;
import com.overnetcontact.dvvs.service.dto.SvcPlanDTO;
import com.overnetcontact.dvvs.service.mapper.SvcPlanMapper;
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
 * Service for executing complex queries for {@link SvcPlan} entities in the database.
 * The main input is a {@link SvcPlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SvcPlanDTO} or a {@link Page} of {@link SvcPlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SvcPlanQueryService extends QueryService<SvcPlan> {

    private final Logger log = LoggerFactory.getLogger(SvcPlanQueryService.class);

    private final SvcPlanRepository svcPlanRepository;

    private final SvcPlanMapper svcPlanMapper;

    public SvcPlanQueryService(SvcPlanRepository svcPlanRepository, SvcPlanMapper svcPlanMapper) {
        this.svcPlanRepository = svcPlanRepository;
        this.svcPlanMapper = svcPlanMapper;
    }

    /**
     * Return a {@link List} of {@link SvcPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SvcPlanDTO> findByCriteria(SvcPlanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SvcPlan> specification = createSpecification(criteria);
        return svcPlanMapper.toDto(svcPlanRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SvcPlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SvcPlanDTO> findByCriteria(SvcPlanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SvcPlan> specification = createSpecification(criteria);
        return svcPlanRepository.findAll(specification, page).map(svcPlanMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SvcPlanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SvcPlan> specification = createSpecification(criteria);
        return svcPlanRepository.count(specification);
    }

    /**
     * Function to convert {@link SvcPlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SvcPlan> createSpecification(SvcPlanCriteria criteria) {
        Specification<SvcPlan> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SvcPlan_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SvcPlan_.name));
            }
            if (criteria.getServiceManagerId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getServiceManagerId(), SvcPlan_.serviceManagerId));
            }
            if (criteria.getDefaultSuppervisorId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDefaultSuppervisorId(), SvcPlan_.defaultSuppervisorId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), SvcPlan_.status));
            }
            if (criteria.getStartPlan() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartPlan(), SvcPlan_.startPlan));
            }
            if (criteria.getEndPlan() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndPlan(), SvcPlan_.endPlan));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), SvcPlan_.createDate));
            }
            if (criteria.getContractId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getContractId(), SvcPlan_.contractId));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), SvcPlan_.note));
            }
            if (criteria.getSvcPlanUnitId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSvcPlanUnitId(),
                            root -> root.join(SvcPlan_.svcPlanUnits, JoinType.LEFT).get(SvcPlanUnit_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
